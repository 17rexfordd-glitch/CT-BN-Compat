import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.nio.charset.StandardCharsets;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/** Inspects the packaged classfiles, without loading Minecraft or mixin classes. */
public class VerifyJar {
    static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        System.out.println("PASS " + message);
    }
    static Map<String, byte[]> entries(String path) throws Exception {
        Map<String, byte[]> result = new TreeMap<>();
        try (JarFile jar = new JarFile(path)) {
            var all = jar.entries();
            while (all.hasMoreElements()) {
                var e = all.nextElement();
                if (e.isDirectory()) continue;
                if (result.put(e.getName(), jar.getInputStream(e).readAllBytes()) != null)
                    throw new AssertionError("Duplicate entry: " + e.getName());
            }
        }
        return result;
    }
    static ClassNode node(byte[] bytes) {
        var n = new ClassNode(); new ClassReader(bytes).accept(n, 0); return n;
    }
    static Object value(AnnotationNode n, String key) {
        if (n == null || n.values == null) return null;
        for (int i=0;i<n.values.size();i+=2) if (key.equals(n.values.get(i))) return n.values.get(i+1);
        return null;
    }
    static AnnotationNode annotation(List<AnnotationNode> ns, String desc) {
        if (ns != null) for (var n:ns) if (desc.equals(n.desc)) return n;
        return null;
    }
    static String canonical(Object o) {
        if (o instanceof AnnotationNode a) {
            var fields = new TreeMap<String,String>();
            if(a.values!=null) for(int i=0;i<a.values.size();i+=2) {
                Object v=a.values.get(i+1);
                // Historical JAR encoded Inject.at as a scalar; the real API is At[].
                if(a.desc.endsWith("/Inject;") && a.values.get(i).equals("at") && v instanceof AnnotationNode) v=List.of(v);
                fields.put((String)a.values.get(i),canonical(v));
            }
            return a.desc+fields;
        }
        if (o instanceof List<?> l) return l.stream().map(VerifyJar::canonical).toList().toString();
        if (o instanceof String[] a) return Arrays.toString(a);
        return String.valueOf(o);
    }
    static List<String> hooks(ClassNode n) {
        var result = new ArrayList<String>();
        result.add(canonical(annotation(n.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;")));
        for (var m:n.methods) {
            for (var list:Arrays.asList(m.visibleAnnotations,m.invisibleAnnotations)) if (list != null)
                for (var a:list) if (a.desc.startsWith("Lorg/spongepowered/"))
                    result.add(m.name+m.desc+canonical(a));
        }
        for (var f:n.fields) {
            for (var list:Arrays.asList(f.visibleAnnotations,f.invisibleAnnotations)) if (list != null)
                for (var a:list) if (a.desc.startsWith("Lorg/spongepowered/"))
                    result.add(f.name+f.desc+canonical(a));
        }
        Collections.sort(result); return result;
    }
    public static void main(String[] args) throws Exception {
        var base = entries(args[0]); var built = entries(args[1]);
        check(true,"JAR has no duplicate entries");
        check(Arrays.equals(base.get("ct_bn_v2.mixins.json"),built.get("ct_bn_v2.mixins.json")),"Mixin configuration unchanged");
        check(new String(built.get("META-INF/neoforge.mods.toml"),StandardCharsets.UTF_8).contains("version=\"2.2.0\""),"Packaged mod metadata version 2.2.0");
        var refs = new HashSet<String>(); int mixins=0, classes=0;
        for (var e:built.entrySet()) if(e.getKey().endsWith(".class")) {
            classes++;
            check(e.getKey().startsWith("dev/devun/ctbncompat/"),"Only project class: "+e.getKey());
            var n=node(e.getValue());
            check(n.version==65,"Java 21 classfile: "+n.name);
            String raw=new String(e.getValue(),StandardCharsets.ISO_8859_1);
            check(!raw.contains("java/lang/reflect") && !raw.contains("EventBusSubscriber") && !raw.contains("NeoForge$EventBus"),"No reflection, automatic subscriber, or fake event bus: "+n.name);
            check(!raw.matches("(?s).*v?2\\.(?:0\\.\\d+|1\\.[0-3])(?:[^0-9]|$).*"),"No stale executable version: "+n.name);
            if(!n.name.contains("/mixin/")) check(!raw.contains("dev/devun/ctbncompat/mixin/"),"No runtime reference to mixin package: "+n.name);
            if(n.name.contains("/mixin/")) {
                mixins++;
                check(annotation(n.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;")!=null,"CLASS-retention @Mixin: "+n.name);
                check(annotation(n.visibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;")==null,"No runtime-visible @Mixin: "+n.name);
                check(hooks(n).equals(hooks(node(base.get(e.getKey())))),"Baseline targets, injection descriptors, and shadow annotations preserved: "+n.name);
            }
            var mod = annotation(n.visibleAnnotations,"Lnet/neoforged/fml/common/Mod;");
            if(mod!=null) {
                var dist=value(mod,"dist");
                check(dist instanceof List<?> && ((List<?>)dist).size()==1 && canonical(dist).contains("CLIENT"),"@Mod.dist encoded as one-element CLIENT enum array");
            }
            for(var m:n.methods) for(var i:m.instructions) {
                if(i instanceof FieldInsnNode f) refs.add(f.getOpcode()+" "+f.owner+"."+f.name+":"+f.desc);
                if(i instanceof MethodInsnNode c) refs.add(c.getOpcode()+" "+c.owner+"."+c.name+c.desc);
            }
        }
        check(mixins==13,"All 13 configured Mixins packaged");
        check(classes==base.keySet().stream().filter(s->s.endsWith(".class")).count(),"Baseline class set size preserved");
        String all=String.join("\n",refs);
        check(refs.contains("178 net/neoforged/neoforge/common/NeoForge.EVENT_BUS:Lnet/neoforged/bus/api/IEventBus;"),"GETSTATIC NeoForge.EVENT_BUS:IEventBus");
        check(refs.contains("180 net/minecraft/world/inventory/AbstractContainerMenu.slots:Lnet/minecraft/core/NonNullList;"),"GETFIELD AbstractContainerMenu.slots:NonNullList");
        check(all.contains("185 net/neoforged/bus/api/IEventBus.addListener"),"Listener registration invokes IEventBus interface");
        check(all.contains("IModInfo.getVersion()Lorg/apache/maven/artifact/versioning/ArtifactVersion;"),"IModInfo.getVersion:ArtifactVersion");
        check(all.contains("BuiltInRegistries.ITEM:Lnet/minecraft/core/DefaultedRegistry;"),"BuiltInRegistries.ITEM:DefaultedRegistry");
        check(all.contains("ItemStack.getComponents()Lnet/minecraft/core/component/DataComponentMap;"),"ItemStack.getComponents:DataComponentMap");
        check(!all.contains("AbstractContainerMenu.slots:Ljava/util/List;") && !all.contains("IModInfo.getVersion()Ljava/lang/Object;") && !all.contains("BuiltInRegistries.ITEM:Lnet/minecraft/core/Registry;") && !all.contains("ItemStack.getComponents()Ljava/lang/Object;"),"Known bad descriptors absent");
        String main=new String(built.get("dev/devun/ctbncompat/ColorTooltipsBetterNetherCompat.class"),StandardCharsets.ISO_8859_1);
        String plugin=new String(built.get("dev/devun/ctbncompat/DiagnosticMixinPlugin.class"),StandardCharsets.ISO_8859_1);
        check(main.contains("v2.2.0 diagnostics ACTIVE") && main.contains("behavioral-fixes=false") && main.contains("CLIENT-RUNTIME-ANCHOR REGISTERED"),"Main version, diagnostic-only flag, explicit runtime anchor");
        check(plugin.contains("MIXIN-PLUGIN-BOOTSTRAP-SAFE version=2.2.0") && !plugin.contains("net/neoforged/fml/ModList"),"Bootstrap version 2.2.0; no bootstrap ModList reference");
        Files.write(Path.of(args[2]),new TreeSet<>(refs));
        System.out.println("VERIFIED "+classes+" packaged classes; instruction references written to "+args[2]);
    }
}
