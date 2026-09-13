import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.nio.charset.StandardCharsets;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
public class FindTooltipListeners {
 public static void main(String[] args)throws Exception {
  for(String path:Files.readAllLines(Path.of(args[0])))try(var jar=new JarFile(path)) {
   var es=jar.entries();while(es.hasMoreElements()) {
    var e=es.nextElement();if(!e.getName().endsWith(".class"))continue;
    byte[] b=jar.getInputStream(e).readAllBytes();
    if(!new String(b,StandardCharsets.ISO_8859_1).contains("ItemTooltipEvent"))continue;
    var n=new ClassNode();new ClassReader(b).accept(n,ClassReader.SKIP_DEBUG|ClassReader.SKIP_FRAMES);
    for(var m:n.methods)if(m.desc.equals("(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V")) {
     boolean annotated=m.visibleAnnotations!=null&&m.visibleAnnotations.stream().anyMatch(a->a.desc.equals("Lnet/neoforged/bus/api/SubscribeEvent;"));
     if(annotated)System.out.println(Path.of(path).getFileName()+"\t"+n.name+"\t"+m.name+"\t"+m.access);
    }
   }
  }
 }
}
