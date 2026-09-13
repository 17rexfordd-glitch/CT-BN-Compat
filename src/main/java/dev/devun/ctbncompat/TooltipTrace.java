package dev.devun.ctbncompat;

import com.mojang.datafixers.util.Either;
import dev.devun.ctbncompat.bridge.ClientTextTooltipBridge;
import dev.devun.ctbncompat.trace.TraceModel;
import dev.devun.ctbncompat.trace.TraceModel.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Component;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import com.google.gson.*;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Read-only adapter. Event identity is used only to pair HEAD/RETURN, never to dedupe. */
public final class TooltipTrace {
    private static final Recorder TRACE = new Recorder(DiagnosticLog::log);
    private static final Map<String,String> NORMAL_STATES = new HashMap<>();
    private static final Map<String,Long> NORMAL_TIMES = new HashMap<>();
    private static final ThreadLocal<Deque<Pending>> PENDING = ThreadLocal.withInitial(ArrayDeque::new);
    private static ItemStack logicalCopy;
    private static String logical="none";
    private static long frame, hoverFrame=-100, epoch, eventSequence;
    private static Object screen;
    private static HolderLookup.Provider serializationContext=RegistryAccess.EMPTY;
    private record Pending(String owner,ItemTooltipEvent event,long token,long frame,String logical,Snapshot before,String evidence) {}
    private TooltipTrace() {}
    public static void beginFrame(long next,Object currentScreen) {
        Minecraft client=Minecraft.getInstance();
        beginFrame(next,currentScreen,client.level==null?RegistryAccess.EMPTY:client.level.registryAccess());
    }
    static void beginFrame(long next,Object currentScreen,HolderLookup.Provider registries) {
        frame=next;
        UpstreamTrace.beginFrame();
        serializationContext=registries;
        if(currentScreen!=screen) { logicalCopy=null;hoverFrame=-100;screen=currentScreen; }
        // HEAD without RETURN (e.g. a throwing handler) must not retain an event indefinitely.
        PENDING.get().clear();
    }
    public static String signature(ItemStack stack) {
        return DiagnosticLog.itemId(stack)+"/count="+(stack==null?0:stack.getCount())+"/componentHash="+DiagnosticLog.componentHash(stack);
    }
    private static boolean sameItem(ItemStack a,ItemStack b) {
        return a!=null&&b!=null&&a.getCount()==b.getCount()&&ItemStack.isSameItemSameComponents(a,b);
    }
    public static boolean recent() { return logicalCopy!=null&&frame==hoverFrame; }
    public static boolean matchesHover(ItemStack stack) { return recent()&&sameItem(stack,logicalCopy); }
    public static void hover(ItemStack stack,int x,int y) {
        if(stack==null||stack.isEmpty()){logicalCopy=null;hoverFrame=-100;return;}
        if(!sameItem(stack,logicalCopy)) {
            logicalCopy=stack.copy();epoch++;
            logical=signature(stack)+"/logicalRevision="+epoch;
        }
        if(frame-hoverFrame>1) { NORMAL_STATES.clear();NORMAL_TIMES.clear(); }
        hoverFrame=frame;
        TRACE.begin(frame,logical);
        TRACE.chain("JEI-INGREDIENT-HOVER path=IngredientGrid.drawTooltip recentJeiHover=true contents=not-yet-created mouse="+x+","+y+" ITEM-IDENTITY="+identity(stack));
    }
    private static String identity(Object o) { return o==null?"null":Integer.toHexString(System.identityHashCode(o)); }
    public static void normal(String key,String state,String message) {
        long now=System.nanoTime();Long then=NORMAL_TIMES.get(key);
        if(!state.equals(NORMAL_STATES.get(key))||then==null||now-then>=5_000_000_000L) {
            DiagnosticLog.log(message+" frame="+frame+" recentJeiHover="+recent());
            NORMAL_STATES.put(key,state);NORMAL_TIMES.put(key,now);
        }
    }
    public static void point(String path,ItemStack stack,Object tooltip) {
        point(path,stack,tooltip,"");
    }
    public static void point(String path,ItemStack stack,Object tooltip,String evidence) {
        if(!recent()) { normal(path,signature(stack),"DIAGNOSTIC-PATH path="+path+" item="+signature(stack));return; }
        if(stack!=null&&!sameItem(stack,logicalCopy)) {
            normal("mismatch:"+path,signature(stack),"JEI-CHAIN-MISMATCH path="+path+" observed="+signature(stack)+" expected="+logical+" ITEM-IDENTITY="+identity(stack));return;
        }
        if(tooltip==null) {
            TRACE.chain("JEI-SUBSEQUENT-INVOKE path="+path+" recentJeiHover=true contents=not-exposed inheritedTooltipSHA256="+TRACE.inheritedFingerprint()+" ITEM-IDENTITY="+identity(stack)+" "+evidence);return;
        }
        TRACE.stage(path,snapshot(tooltip),System.nanoTime(),"recentJeiHover=true ITEM-IDENTITY="+identity(stack)+" "+evidence);
    }
    public static void before(String owner,ItemTooltipEvent event) {
        if(!recent()||!sameItem(event.getItemStack(),logicalCopy))return;
        Snapshot before=snapshot(event.getToolTip());
        Deque<Pending> pending=PENDING.get();
        if(pending.size()>=64) { normal("depth-limit","limit","TRACE-INCOMPLETE reason=handler-depth-limit");return; }
        long token=++eventSequence;
        String evidence=UpstreamTrace.handlerEvidence(owner,event);
        pending.push(new Pending(owner,event,token,frame,logical,before,evidence));
        TRACE.stage(owner+".BEFORE",before,System.nanoTime(),"event="+token+" ITEM-IDENTITY="+identity(event.getItemStack())+" "+evidence);
    }
    public static void after(String owner,ItemTooltipEvent event) {
        Deque<Pending> pending=PENDING.get();
        Pending match=null;
        for(var it=pending.iterator();it.hasNext();) {
            Pending p=it.next();if(p.owner.equals(owner)&&p.event==event){match=p;it.remove();break;}
        }
        if(match==null||match.frame!=frame||!match.logical.equals(logical))return;
        Snapshot after=snapshot(event.getToolTip());
        TRACE.stage(owner+".AFTER",after,System.nanoTime(),"event="+match.token+" ITEM-IDENTITY="+identity(event.getItemStack())+" "+match.evidence);
        TRACE.pair(owner,match.token,match.before,after,System.nanoTime(),"item="+signature(event.getItemStack())+" logicalItemChanged="+!sameItem(event.getItemStack(),logicalCopy)+" "+match.evidence);
    }
    public static Snapshot snapshot(Object tooltip) {
        List<Element> elements=new ArrayList<>();
        if(tooltip instanceof List<?> list)for(Object e:list)elements.add(element(e));
        else elements.add(element(tooltip));
        return Snapshot.of(elements);
    }
    public static Element element(Object original) {
        try { return readElement(original); }
        catch (RuntimeException failure) {
            String type=original==null?"null":original.getClass().getName();
            String error=failure.getClass().getName();
            return new Element(type,"unreadable:"+error,TraceModel.hash(type+error),"snapshot-error-partial");
        }
    }
    private static Element readElement(Object original) {
        Object value=original;
        if(value instanceof Either<?,?> either)value=either.map(v->v,v->v);
        String type=value==null?"null":value.getClass().getName().replaceAll("/0x[0-9a-fA-F]+$", "");
        StringBuilder text=new StringBuilder(), styled=new StringBuilder();
        String coverage="styled-text";
        if(value instanceof FormattedText ft) {
            ft.visit((style,s)->{s.codePoints().forEach(cp->append(text,styled,style,cp));return Optional.empty();},Style.EMPTY);
        } else if(value instanceof ClientTextTooltipBridge bridge) {
            bridge.ctbn$getText().accept((index,style,cp)->{append(text,styled,style,cp);return true;});
        } else if(value instanceof FormattedCharSequence seq) {
            seq.accept((index,style,cp)->{append(text,styled,style,cp);return true;});
        } else if(value instanceof String s) { text.append(s);styled.append(s);coverage="plain-text";
        } else if(value instanceof ClientTooltipComponent visual) {
            // Unknown visuals are explicitly partial; identity/toString/hashCode would invent changes.
            coverage="opaque-visual-type-and-size";
            styled.append("width=").append(visual.getWidth(Minecraft.getInstance().font)).append(";height=").append(visual.getHeight());
            text.append(styled);
        } else { coverage="opaque-type-only";styled.append(type); }
        String representation=styled.toString();
        if(value instanceof Component component) {
            try {
                String json=Component.Serializer.toJson(component,serializationContext);
                representation=canonical(JsonParser.parseString(json));
                styled.append(";component-json=").append(representation);
                coverage="full-component-json-and-styled-text";
            } catch (RuntimeException unsupported) {
                coverage="styled-text-partial-serialization-unavailable";
            }
        }
        return new Element(type,text.toString(),TraceModel.hash(styled.toString()),coverage,representation);
    }
    private static String canonical(JsonElement json) {
        if(json.isJsonObject()) {
            JsonObject sorted=new JsonObject();
            new TreeSet<>(json.getAsJsonObject().keySet()).forEach(k->sorted.add(k,JsonParser.parseString(canonical(json.getAsJsonObject().get(k)))));
            return sorted.toString();
        }
        if(json.isJsonArray()) {
            JsonArray array=new JsonArray();for(JsonElement e:json.getAsJsonArray())array.add(JsonParser.parseString(canonical(e)));return array.toString();
        }
        return json.toString();
    }
    private static void append(StringBuilder text,StringBuilder styled,Style s,int cp) {
        text.appendCodePoint(cp);
        styled.append(cp).append(':').append(s.getColor()==null?"default":s.getColor().getValue()).append(':')
                .append(s.isBold()).append(s.isItalic()).append(s.isUnderlined()).append(s.isStrikethrough()).append(s.isObfuscated())
                .append(':').append(s.getFont()).append(';');
    }
}
