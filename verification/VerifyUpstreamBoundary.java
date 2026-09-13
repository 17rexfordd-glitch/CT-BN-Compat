import java.util.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
public class VerifyUpstreamBoundary {
 static final String EVENT="Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;";
 static final String PARAMETERS="(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/world/item/Item$TooltipContext;)";
 static void check(boolean value,String msg){VerifyJar.check(value,msg);}
 static MethodNode method(ClassNode c,String name,String desc){return c.methods.stream().filter(m->m.name.equals(name)&&m.desc.equals(desc)).findFirst().orElseThrow();}
 static List<MethodInsnNode> calls(MethodNode method){var result=new ArrayList<MethodInsnNode>();for(var i:method.instructions)if(i instanceof MethodInsnNode m)result.add(m);return result;}
 public static void main(String[] args)throws Exception {
  var mc=VerifyJar.entries(args[0]);var neo=VerifyJar.entries(args[1]);var jar=VerifyJar.entries(args[2]);
  String tooltipDesc="(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;";
  var generate=method(VerifyJar.node(mc.get("net/minecraft/world/item/ItemStack.class")),"getTooltipLines",tooltipDesc);
  check(calls(generate).stream().filter(c->c.owner.equals("net/neoforged/neoforge/event/EventHooks")&&c.name.equals("onItemTooltip")&&c.desc.equals(PARAMETERS+EVENT)&&c.getOpcode()==Opcodes.INVOKESTATIC).count()==1,"Real ItemStack.getTooltipLines invokes exact EventHooks.onItemTooltip once");
  var eventClass=VerifyJar.node(neo.get("net/neoforged/neoforge/event/entity/player/ItemTooltipEvent.class"));
  method(eventClass,"<init>",PARAMETERS+"V");check(true,"Real ItemTooltipEvent constructor descriptor verified");
  var hook=method(VerifyJar.node(neo.get("net/neoforged/neoforge/event/EventHooks.class")),"onItemTooltip",PARAMETERS+EVENT);
  var calls=calls(hook);int ctor=-1,post=-1;
  for(int i=0;i<calls.size();i++) {
   var c=calls.get(i);
   if(c.owner.equals(eventClass.name)&&c.name.equals("<init>"))ctor=i;
   if(c.owner.equals("net/neoforged/bus/api/IEventBus")&&c.name.equals("post")) {
    check(post==-1,"Single event bus post invocation");post=i;
    check(c.getOpcode()==Opcodes.INVOKEINTERFACE&&c.desc.equals("(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;"),"Real interface post descriptor/opcode verified");
   }
  }
  check(ctor>=0&&post>ctor,"Event constructed before dispatch in real bytecode");
  var mixin=VerifyJar.node(jar.get("dev/devun/ctbncompat/mixin/EventHooksTooltipMixin.class"));
  int before=0,after=0;
  for(var m:mixin.methods) {
   var a=VerifyJar.annotation(m.visibleAnnotations,"Lorg/spongepowered/asm/mixin/injection/Inject;");if(a==null)continue;
   for(Object value:(List<?>)VerifyJar.value(a,"at")) {
    var at=(AnnotationNode)value;
    if("INVOKE".equals(VerifyJar.value(at,"value"))) {
     check(VerifyJar.value(at,"target").equals("Lnet/neoforged/bus/api/IEventBus;post(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;"),"Packaged At target matches actual invocation");
     String shift=VerifyJar.canonical(VerifyJar.value(at,"shift"));if(shift.contains("BEFORE"))before++;if(shift.contains("AFTER"))after++;
    }
   }
  }
  check(before==1&&after==1,"Packaged pre/post observers straddle the single real post call");
 }
}
