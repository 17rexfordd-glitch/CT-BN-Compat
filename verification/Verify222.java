import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
public class Verify222 {
    static void ok(boolean v,String m) { VerifyJar.check(v,m); }
    static Set<String> injections(ClassNode n) {
        var result=new HashSet<String>();
        for(var m:n.methods)if(m.visibleAnnotations!=null)for(var a:m.visibleAnnotations)
            if(a.desc.equals("Lorg/spongepowered/asm/mixin/injection/Inject;"))result.add(VerifyJar.canonical(a));
        return result;
    }
    public static void main(String[] args)throws Exception {
        var base=VerifyJar.entries(args[0]);var built=VerifyJar.entries(args[1]);
        String config=new String(built.get("ct_bn_v2.mixins.json"),StandardCharsets.UTF_8);
        var names=new HashSet<String>();var matcher=Pattern.compile("\"(\\w+Mixin)\"").matcher(config);
        while(matcher.find())names.add(matcher.group(1));
        ok(names.size()==47,"47 configured Mixins: 14 preserved, 3 upstream boundaries, 30 optional verified handlers");
        Set<String> refs=new HashSet<>();int classes=0;
        for(var e:built.entrySet())if(e.getKey().endsWith(".class")) {
            classes++;ClassNode n=VerifyJar.node(e.getValue());String raw=new String(e.getValue(),StandardCharsets.ISO_8859_1);
            ok(n.version==65&&n.name.startsWith("dev/devun/ctbncompat/"),"Java 21 project-only class "+n.name);
            ok(!raw.contains("java/lang/reflect")&&!raw.contains("EventBusSubscriber")&&!raw.contains("NeoForge$EventBus"),"No reflection, auto subscriber, or fake bus "+n.name);
            ok(!raw.contains("2.2.1")&&!raw.contains("version=2.1."),"No stale version marker "+n.name);
            if(!n.name.contains("/mixin/"))ok(!raw.contains("dev/devun/ctbncompat/mixin/"),"Runtime does not reference mixin package "+n.name);
            else {
                ok(names.contains(n.name.substring(n.name.lastIndexOf('/')+1)),"Mixin configured "+n.name);
                ok(VerifyJar.annotation(n.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;")!=null,"CLASS retention @Mixin "+n.name);
                if(base.containsKey(e.getKey())) {
                    var old=VerifyJar.node(base.get(e.getKey()));
                    ok(injections(n).containsAll(injections(old)),"All previous injection selectors retained "+n.name);
                    ok(VerifyJar.canonical(VerifyJar.annotation(n.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;")).equals(VerifyJar.canonical(VerifyJar.annotation(old.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;"))),"Target classes unchanged "+n.name);
                }
                ok(!raw.contains("/Redirect;")&&!raw.contains("/Modify")&&!raw.contains("setReturnValue")&&!raw.contains("cancel"),"No behavior-changing injection "+n.name);
            }
            var mod=VerifyJar.annotation(n.visibleAnnotations,"Lnet/neoforged/fml/common/Mod;");
            if(mod!=null)ok(VerifyJar.value(mod,"dist") instanceof List<?> l&&l.size()==1&&VerifyJar.canonical(l).contains("CLIENT"),"@Mod.dist CLIENT array");
            for(var m:n.methods)for(var i:m.instructions) {
                if(i instanceof FieldInsnNode f) {
                    refs.add(f.getOpcode()+" "+f.owner+"."+f.name+":"+f.desc);
                    ok(!(f.getOpcode()==Opcodes.PUTFIELD||f.getOpcode()==Opcodes.PUTSTATIC)||f.owner.startsWith("dev/devun/ctbncompat/"),"Field writes confined to diagnostics "+n.name+"."+m.name);
                }
                if(i instanceof MethodInsnNode c) {
                    refs.add(c.getOpcode()+" "+c.owner+"."+c.name+c.desc);
                    ok(!c.name.equals("cancel")&&!c.name.equals("setReturnValue"),"No callback behavior override "+n.name+"."+m.name);
                }
            }
        }
        String all=String.join("\n",refs);
        ok(refs.contains("178 net/neoforged/neoforge/common/NeoForge.EVENT_BUS:Lnet/neoforged/bus/api/IEventBus;"),"EVENT_BUS descriptor preserved");
        ok(refs.contains("180 net/minecraft/world/inventory/AbstractContainerMenu.slots:Lnet/minecraft/core/NonNullList;"),"slots descriptor preserved");
        ok(!all.contains("slots:Ljava/util/List;")&&!all.contains("getVersion()Ljava/lang/Object;")&&!all.contains("getComponents()Ljava/lang/Object;")&&!all.contains("getKey(Ljava/lang/Object;)Ljava/lang/Object;"),"Prior bad descriptors absent");
        String main=new String(built.get("dev/devun/ctbncompat/ColorTooltipsBetterNetherCompat.class"),StandardCharsets.ISO_8859_1);
        ok(main.contains("v2.2.2 diagnostics ACTIVE")&&main.contains("behavioral-fixes=false")&&main.contains("CLIENT-RUNTIME-ANCHOR REGISTERED"),"Main version, diagnostic-only flag, and runtime anchor");
        String plugin=new String(built.get("dev/devun/ctbncompat/DiagnosticMixinPlugin.class"),StandardCharsets.ISO_8859_1);
        ok(plugin.contains("version=2.2.2")&&!plugin.contains("net/neoforged/fml/ModList"),"Bootstrap marker and deferred ModList");
        for(String owner:List.of("BetterAdvancedTooltipsMixin","IconLeadingTooltipMixin")) {
            var n=VerifyJar.node(built.get("dev/devun/ctbncompat/mixin/"+owner+".class"));
            ok(injections(n).stream().anyMatch(s->s.contains("HEAD"))&&injections(n).stream().anyMatch(s->s.contains("RETURN")),"Paired handler snapshots "+owner);
            ok(n.methods.stream().filter(m->m.name.startsWith("ctbn$")).allMatch(m->m.desc.startsWith("(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;")),"Same typed event passed to both handler hooks "+owner);
        }
        ok(new String(built.get("META-INF/neoforge.mods.toml"),StandardCharsets.UTF_8).contains("version=\"2.2.2\""),"Packaged metadata 2.2.2");
        Files.write(Path.of(args[2]),new TreeSet<>(refs));
        System.out.println("VERIFIED "+classes+" final packaged classes; no duplicate JAR entries; retained hooks and read-only mutations audit passed.");
    }
}
