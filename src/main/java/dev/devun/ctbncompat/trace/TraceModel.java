package dev.devun.ctbncompat.trace;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Immutable diagnostic data and bounded emission policy. No Minecraft state is modified. */
public final class TraceModel {
    private TraceModel() {}
    public static String hash(String value) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
    }
    public static String brief(String value) {
        String escaped = value.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t").replace("\"", "\\\"");
        return escaped.length() <= 120 ? escaped : escaped.substring(0, 120) + "...";
    }
    public record Element(String type, String text, String hash, String coverage,String representation) {
        public Element(String type,String text,String hash,String coverage) { this(type,text,hash,coverage,""); }
        public String describe(int index) { return "{index="+index+",type="+type+",text=\""+brief(text)+"\",sha256="+hash+",coverage="+coverage+",representation=\""+brief(representation)+"\"}"; }
    }
    public record Snapshot(List<Element> elements, String hash) {
        public Snapshot { elements = List.copyOf(elements); }
        public static Snapshot of(List<Element> elements) {
            StringBuilder key = new StringBuilder();
            for (Element e : elements) key.append(e.type.length()).append(':').append(e.type).append(':').append(e.hash).append(';');
            return new Snapshot(elements, TraceModel.hash(key.toString()));
        }
        public String summary() { return "tooltipCount="+elements.size()+" orderedSHA256="+hash; }
        public String describe() {
            List<String> out = new ArrayList<>();
            for (int i=0;i<Math.min(8,elements.size());i++) out.add(elements.get(i).describe(i));
            return out+" omitted="+Math.max(0,elements.size()-8);
        }
    }
    private static boolean same(Element a, Element b) { return a.type.equals(b.type) && a.hash.equals(b.hash); }
    public static String diff(Snapshot before, Snapshot after) {
        List<Element> a=before.elements, b=after.elements;
        List<String> added=new ArrayList<>(),removed=new ArrayList<>(),modified=new ArrayList<>();
        // LCS prevents one inserted line from appearing to modify every subsequent line.
        if ((long)a.size()*b.size() <= 65536) {
            int[][] lcs=new int[a.size()+1][b.size()+1];
            for(int i=a.size()-1;i>=0;i--)for(int j=b.size()-1;j>=0;j--)
                lcs[i][j]=same(a.get(i),b.get(j))?1+lcs[i+1][j+1]:Math.max(lcs[i+1][j],lcs[i][j+1]);
            int i=0,j=0;
            List<Integer> oldGap=new ArrayList<>(),newGap=new ArrayList<>();
            while(i<a.size()||j<b.size()) {
                if(i<a.size()&&j<b.size()&&same(a.get(i),b.get(j))) {
                    flush(a,b,oldGap,newGap,added,removed,modified);i++;j++;
                } else if(j<b.size()&&(i==a.size()||lcs[i][j+1]>lcs[i+1][j]))newGap.add(j++);
                else oldGap.add(i++);
            }
            flush(a,b,oldGap,newGap,added,removed,modified);
        } else {
            // Bounded memory fallback; indices remain exact but shifted lines may be modifications.
            for(int i=0;i<Math.max(a.size(),b.size());i++) {
                if(i>=a.size())added.add(b.get(i).describe(i));
                else if(i>=b.size())removed.add(a.get(i).describe(i));
                else if(!same(a.get(i),b.get(i)))modified.add(modification(a.get(i),i,b.get(i),i));
            }
        }
        return "added="+limited(added)+" removed="+limited(removed)+" modified="+limited(modified)
                +" alignment="+((long)a.size()*b.size()<=65536?"LCS":"positional-large-list");
    }
    private static void flush(List<Element>a,List<Element>b,List<Integer>x,List<Integer>y,List<String>added,List<String>removed,List<String>modified) {
        int paired=Math.min(x.size(),y.size());
        for(int k=0;k<paired;k++)modified.add(modification(a.get(x.get(k)),x.get(k),b.get(y.get(k)),y.get(k)));
        for(int k=paired;k<x.size();k++)removed.add(a.get(x.get(k)).describe(x.get(k)));
        for(int k=paired;k<y.size();k++)added.add(b.get(y.get(k)).describe(y.get(k)));
        x.clear();y.clear();
    }
    private static String limited(List<String> xs) { return xs.subList(0,Math.min(8,xs.size()))+"(total="+xs.size()+")"; }
    private static String modification(Element a,int oldIndex,Element b,int newIndex) {
        String extra="";
        if(!a.representation.equals(b.representation)) {
            int offset=0,common=Math.min(a.representation.length(),b.representation.length());
            while(offset<common&&a.representation.charAt(offset)==b.representation.charAt(offset))offset++;
            int start=Math.max(0,offset-30);
            extra=" representationFirstDifference="+offset+" before=\""+brief(a.representation.substring(start,Math.min(a.representation.length(),offset+70)))
                    +"\" after=\""+brief(b.representation.substring(start,Math.min(b.representation.length(),offset+70)))+"\"";
        }
        return a.describe(oldIndex)+"->"+b.describe(newIndex)+extra;
    }

    public static final class Recorder {
        private final Consumer<String> sink;
        private final Map<String,String> last=new HashMap<>();
        private final Map<String,Long> times=new HashMap<>();
        private Map<String,Snapshot> previous=new HashMap<>(),current=new HashMap<>();
        private final Map<String,Integer> occurrences=new HashMap<>();
        private long frame=-1,session;
        private String logical="",firstMutation="";
        private String inherited="not-yet-sampled";
        private boolean fullFrame;
        private long lastFullNs;
        private String lastMutationOwner="";
        private final List<Supplier<String>> buffered=new ArrayList<>();
        private final Map<String,Boolean> outcomes=new HashMap<>();
        public Recorder(Consumer<String> sink) { this.sink=sink; }
        public void begin(long frame,String logical) {
            begin(frame,logical,System.nanoTime());
        }
        public void begin(long frame,String logical,long now) {
            boolean fresh=!this.logical.equals(logical)||frame>this.frame+1;
            if(fresh) { previous.clear();current.clear();last.clear();times.clear();outcomes.clear();lastMutationOwner="";session++; }
            if(this.frame!=frame||fresh) {
                previous=current;current=new HashMap<>();occurrences.clear();firstMutation="";inherited="not-yet-sampled";
                buffered.clear();fullFrame=fresh||now-lastFullNs>=5_000_000_000L;
                if(fullFrame)lastFullNs=now;
            }
            this.frame=frame;this.logical=logical;
        }
        public String context() { return " session="+session+" frame="+frame+" logicalItem="+logical; }
        public String inheritedFingerprint() { return inherited; }
        public void chain(String message) { chain(()->message); }
        private void chain(Supplier<String> message) {
            if(fullFrame)sink.accept(message.get()+context());
            else if(buffered.size()<256)buffered.add(message);
        }
        private static String mutationOwner(String path,String evidence) {
            if(path.equals("ITEMSTACK-TOOLTIP-GENERATED"))return "ITEMSTACK_TOOLTIP_GENERATION";
            if(path.equals("ITEMTOOLTIPEVENT-CREATED"))return "ITEMTOOLTIPEVENT_CONSTRUCTION";
            if(path.equals("ITEMTOOLTIPEVENT-PRE-POST"))return "ITEMTOOLTIPEVENT_PRE_DISPATCH";
            if(path.equals("ITEMTOOLTIPEVENT-POST-POST"))return "EVENTBUS_DISPATCH_UNTRACED_INTERVAL";
            if(path.endsWith(".BEFORE"))return (evidence.contains("verifiedDispatch=true")?"EVENTBUS_DISPATCH_BEFORE:":"UNOBSERVED_INTERVAL_BEFORE:")+path;
            return path;
        }
        public boolean emit(String key,String state,long now,String message) {
            return emit(key,state,now,()->message);
        }
        public boolean emit(String key,String state,long now,Supplier<String> message) {
            Long then=times.get(key);
            if(!state.equals(last.get(key))||then==null||now-then>=5_000_000_000L) {
                sink.accept(message.get()+context());last.put(key,state);times.put(key,now);return true;
            }
            return false;
        }
        public void stage(String path,Snapshot snapshot,long now,String evidence) {
            int occurrence=occurrences.merge(path,1,Integer::sum);
            String key=path+"#"+occurrence;
            // Defensive bound for unexpected reentrant loops; normal paths have only a few occurrences.
            if(occurrence>64)return;
            current.put(key,snapshot);
            inherited=snapshot.hash;
            Snapshot old=previous.get(key);
            boolean changed=old!=null&&!old.hash.equals(snapshot.hash);
            if(changed&&firstMutation.isEmpty()) {
                firstMutation=mutationOwner(path,evidence);
                if(!firstMutation.equals(lastMutationOwner)) {
                    fullFrame=true;lastFullNs=now;
                    for(var message:buffered)sink.accept(message.get()+context());
                    buffered.clear();lastMutationOwner=firstMutation;
                }
                sink.accept("TOOLTIP-FIRST-DIVERGENCE firstMutationOwner="+firstMutation+" attribution=first-observed-point previousFrame="+(frame-1)+" occurrence="+occurrence+" "+evidence+" "+diff(old,snapshot)+context());
            }
            chain(()->"TOOLTIP-TRACE path="+path+" occurrence="+occurrence+" "+snapshot.summary()+" changedFromPreviousFrame="+changed+" "+evidence
                    +(changed?" "+diff(old,snapshot):" elements="+snapshot.describe()));
        }
        public void pair(String owner,long event,Snapshot before,Snapshot after,long now,String evidence) {
            boolean changed=!before.hash.equals(after.hash);
            int occurrence=occurrences.merge("pair:"+owner,1,Integer::sum);
            if(occurrence>64)return;
            String key=owner+"#"+occurrence;
            Boolean previousOutcome=outcomes.put(key,changed);
            // Repeated changed=false is proof about the handler, independent of unstable incoming content.
            if(previousOutcome==null||previousOutcome!=changed||(changed&&fullFrame))
                sink.accept("TOOLTIP-MUTATION owner="+owner+" event="+event+" occurrence="+occurrence+" changed="+changed+" BEFORE["+before.summary()+"] AFTER["+after.summary()+"] "+diff(before,after)+" "+evidence+context());
        }
    }
}
