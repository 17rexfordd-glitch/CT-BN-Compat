package dev.devun.ctbncompat;

import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Correlates actual constructor/post boundaries without replacing dispatch or registering listeners. */
public final class UpstreamTrace {
    private static final ThreadLocal<Deque<Dispatch>> CALLS=ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<Deque<ItemStack>> GENERATION=ThreadLocal.withInitial(ArrayDeque::new);
    private static long eventSequence;
    private static final class Dispatch {
        final ItemStack stack; final List<Component> list; final long token;
        ItemTooltipEvent event; boolean posting;
        int subscriberOrder;
        Dispatch(ItemStack stack,List<Component> list) { this.stack=stack;this.list=list;token=++eventSequence; }
    }
    private UpstreamTrace() {}
    public static void beginFrame() { CALLS.get().clear();GENERATION.get().clear(); }
    public static void itemEntry(ItemStack stack) {
        if(TooltipTrace.matchesHover(stack)) {
            GENERATION.get().push(stack);
            TooltipTrace.point("ITEMSTACK-TOOLTIP-ENTRY",stack,null);
        }
    }
    public static void itemReturn(ItemStack stack) {
        if(GENERATION.get().peek()==stack)GENERATION.get().pop();
    }
    public static void generated(ItemStack stack,List<Component> list) {
        if(!TooltipTrace.matchesHover(stack))return;
        Deque<Dispatch> calls=CALLS.get();
        if(calls.size()>=32){TooltipTrace.normal("dispatch-depth","limit","TRACE-INCOMPLETE reason=dispatch-depth-limit");return;}
        Dispatch d=new Dispatch(stack,list);calls.push(d);
        boolean fromGeneration=GENERATION.get().peek()==stack;
        TooltipTrace.point(fromGeneration?"ITEMSTACK-TOOLTIP-GENERATED":"EVENTHOOKS-TOOLTIP-INPUT",stack,list,"dispatch="+d.token+" boundary=EventHooks.onItemTooltip.HEAD verifiedItemStackGeneration="+fromGeneration);
    }
    public static void created(ItemTooltipEvent event) {
        Dispatch d=CALLS.get().peek();
        if(d==null||event.getToolTip()!=d.list||event.getItemStack()!=d.stack)return;
        d.event=event;
        TooltipTrace.point("ITEMTOOLTIPEVENT-CREATED",d.stack,d.list,"dispatch="+d.token+" boundary=constructor.RETURN");
    }
    public static void prePost(ItemStack stack,List<Component> list) {
        Dispatch d=matching(stack,list);if(d==null)return;
        d.posting=true;
        TooltipTrace.point("ITEMTOOLTIPEVENT-PRE-POST",stack,list,"dispatch="+d.token+" eventCaptured="+(d.event!=null));
    }
    public static void postPost(ItemStack stack,List<Component> list) {
        Dispatch d=matching(stack,list);if(d==null)return;
        d.posting=false;
        TooltipTrace.point("ITEMTOOLTIPEVENT-POST-POST",stack,list,"dispatch="+d.token+" tracedHandlerInvocations="+d.subscriberOrder);
    }
    public static void returned(ItemStack stack,List<Component> list) {
        if(matching(stack,list)!=null)CALLS.get().pop();
    }
    private static Dispatch matching(ItemStack stack,List<Component> list) {
        Dispatch d=CALLS.get().peek();return d!=null&&d.stack==stack&&d.list==list?d:null;
    }
    public static String handlerEvidence(String owner,ItemTooltipEvent event) {
        Dispatch d=CALLS.get().peek();
        if(d==null||!d.posting||d.event!=event)return "verifiedDispatch=false";
        return "verifiedDispatch=true dispatch="+d.token+" tracedHandlerOrder="+(++d.subscriberOrder)+" concreteHandler="+owner
                +" orderScope=instrumented-handler-invocations-not-complete-listener-roster";
    }
    public static boolean dispatched(ItemTooltipEvent event) {
        Dispatch d=CALLS.get().peek();return d!=null&&d.posting&&d.event==event;
    }
    public static void before(String owner,ItemTooltipEvent event) {
        if(dispatched(event))TooltipTrace.before(owner,event);
    }
    public static void after(String owner,ItemTooltipEvent event) {
        if(dispatched(event))TooltipTrace.after(owner,event);
    }
}
