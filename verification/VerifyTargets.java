import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
public class VerifyTargets {
    static Map<String,Path> locations=new HashMap<>();
    static Map<String,ClassNode> cache=new HashMap<>();
    static ClassNode find(String name)throws Exception {
        if(cache.containsKey(name))return cache.get(name);
        byte[] bytes=null;
        Path path=locations.get(name+".class");
        if(path!=null)try(var jar=new JarFile(path.toFile())){bytes=jar.getInputStream(jar.getJarEntry(name+".class")).readAllBytes();}
        else try(var in=ClassLoader.getSystemResourceAsStream(name+".class")){if(in!=null)bytes=in.readAllBytes();}
        if(bytes==null)return null;
        ClassNode n=new ClassNode();new ClassReader(bytes).accept(n,ClassReader.SKIP_CODE|ClassReader.SKIP_DEBUG);cache.put(name,n);return n;
    }
    static boolean member(String owner,String name,String desc,boolean field,Set<String> seen)throws Exception {
        if(!seen.add(owner))return false;var n=find(owner);if(n==null)return false;
        if(field){for(var f:n.fields)if(f.name.equals(name)&&f.desc.equals(desc))return true;}
        else {for(var m:n.methods)if(m.name.equals(name)&&m.desc.equals(desc))return true;}
        if(n.superName!=null&&member(n.superName,name,desc,field,seen))return true;
        for(String i:n.interfaces)if(member(i,name,desc,field,seen))return true;
        return false;
    }
    static void require(boolean ok,String s){if(!ok)throw new AssertionError(s);}
    public static void main(String[] args)throws Exception {
        for(String s:Files.readAllLines(Path.of(args[1]))) {
            Path p=Path.of(s);try(var jar=new JarFile(p.toFile())) {
                var es=jar.entries();while(es.hasMoreElements()){String n=es.nextElement().getName();if(n.endsWith(".class"))locations.putIfAbsent(n,p);}
            }
        }
        var built=VerifyJar.entries(args[0]);int methods=0,fields=0,targets=0;
        for(var e:built.entrySet())if(e.getKey().endsWith(".class"))cache.put(e.getKey().replace(".class",""),VerifyJar.node(e.getValue()));
        for(var e:built.entrySet())if(e.getKey().endsWith(".class")) {
            var n=VerifyJar.node(e.getValue());
            for(var m:n.methods)for(var i:m.instructions) {
                if(i instanceof MethodInsnNode c && (c.owner.startsWith("net/minecraft/")||c.owner.startsWith("net/neoforged/")||c.owner.startsWith("mezz/jei/"))) {
                    require(member(c.owner,c.name,c.desc,false,new HashSet<>()),"Missing method: "+c.owner+"."+c.name+c.desc);
                    var owner=find(c.owner);require(c.itf==((owner.access&Opcodes.ACC_INTERFACE)!=0),"Wrong interface invocation: "+c.owner+"."+c.name);
                    methods++;
                }
                if(i instanceof FieldInsnNode f && (f.owner.startsWith("net/minecraft/")||f.owner.startsWith("net/neoforged/"))) {
                    require(member(f.owner,f.name,f.desc,true,new HashSet<>()),"Missing field: "+f.owner+"."+f.name+f.desc);fields++;
                }
            }
            var mixin=VerifyJar.annotation(n.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Mixin;");
            if(mixin==null)continue;
            var owners=new ArrayList<String>();
            Object values=VerifyJar.value(mixin,"value"), strings=VerifyJar.value(mixin,"targets");
            if(values instanceof List<?> l)for(Object v:l)owners.add(((Type)v).getInternalName());
            if(strings instanceof List<?> l)for(Object v:l)owners.add(v.toString().replace('.','/'));
            for(var m:n.methods) {
                var inject=VerifyJar.annotation(m.visibleAnnotations,"Lorg/spongepowered/asm/mixin/injection/Inject;");
                if(inject==null)continue;
                require(VerifyJar.value(inject,"at") instanceof List<?>,"Inject.at must be annotation array: "+n.name+"."+m.name);
                for(Object selector:(List<?>)VerifyJar.value(inject,"method")) {
                    String s=selector.toString();int split=s.indexOf('(');require(split>0,"Expected exact descriptor: "+s);
                    for(String owner:owners){
                        require(member(owner,s.substring(0,split),s.substring(split),false,new HashSet<>()),"Missing injection target: "+owner+"."+s);
                        System.out.println("PASS injection target "+owner+"."+s);targets++;
                    }
                }
            }
            for(var f:n.fields) {
                var shadow=VerifyJar.annotation(f.visibleAnnotations,"Lorg/spongepowered/asm/mixin/Shadow;");
                if(shadow==null)shadow=VerifyJar.annotation(f.invisibleAnnotations,"Lorg/spongepowered/asm/mixin/Shadow;");
                if(shadow!=null)for(String owner:owners)require(member(owner,f.name,f.desc,true,new HashSet<>()),"Missing shadow: "+owner+"."+f.name);
            }
        }
        System.out.println("PASS real dependency linkage: "+methods+" method instructions, "+fields+" field instructions, "+targets+" exact injection targets; all shadow fields exist; Inject.at arrays verified.");
    }
}
