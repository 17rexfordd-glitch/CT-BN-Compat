package dev.devun.ctbncompat;

import dev.devun.ctbncompat.trace.TraceModel;
import dev.devun.ctbncompat.trace.TraceModel.*;
import dev.devun.ctbncompat.bridge.ClientTextTooltipBridge;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import java.util.*;

/** Headless diagnostic tests: no game launch, no tooltip mutation, no production stubs. */
public final class TraceTests {
    private static int checks;
    private static void check(boolean b,String message) { if(!b)throw new AssertionError(message);checks++; }
    private static Element line(String s) { return new Element("text",s,TraceModel.hash(s),"plain-text"); }
    private static Snapshot lines(String...s) { return Snapshot.of(Arrays.stream(s).map(TraceTests::line).toList()); }
    public static void main(String[] args) {
        net.minecraft.SharedConstants.tryDetectVersion();
        net.neoforged.fml.loading.LoadingModList.of(List.of(),List.of(),List.of(),List.of(),Map.of());
        net.minecraft.server.Bootstrap.bootStrap();
        check(!lines("FB").hash().equals(lines("Ea").hash()),"Java hash collision must not hide a text change");
        check(!lines("a","b").hash().equals(lines("b","a").hash()),"Ordered fingerprint");
        String added=TraceModel.diff(lines("a","c"),lines("a","b","c"));
        check(added.contains("added=[{index=1")&&added.contains("modified=[]"),"Insertion must not shift every line into modified");
        check(TraceModel.diff(lines("a","b","c"),lines("a","c")).contains("removed=[{index=1"),"Removal index");
        check(TraceModel.diff(lines("a","b"),lines("a","x")).contains("modified=[{index=1"),"Modification index");
        var mutable=new ArrayList<>(List.of(line("a")));var snap=Snapshot.of(mutable);mutable.clear();
        check(snap.elements().size()==1,"Before snapshot remains immutable");
        var log=new ArrayList<String>();var r=new Recorder(log::add);
        for(int frame=1;frame<=1000;frame++) {
            long now=frame*16_000_000L;r.begin(frame,"betternether:pickaxe/count=1/components=stable");
            r.emit("grid","logical",now,"grid identity="+frame);
            r.stage("BAT.BEFORE",lines("name"),now,"event="+frame);
            r.stage("BAT.AFTER",lines("name","durability"),now,"event="+frame);
            r.pair("BAT",frame,lines("name"),lines("name","durability"),now,"identity="+frame);
            r.stage("JeiTooltip.draw",lines("name","durability"),now,"");
            r.emit("ColorTooltips.update",r.inheritedFingerprint(),now,"update identity="+frame);
        }
        check(log.size()<=24,"1000 unchanged frames must be quiet, actual records="+log.size());
        check(log.stream().noneMatch(s->s.contains("FIRST-DIVERGENCE")),"Repeated normal handler additions are not consecutive-frame divergence");
        int before=log.size();r.begin(1001,"betternether:pickaxe/count=1/components=stable");
        r.stage("BAT.BEFORE",lines("name"),16_016_000_000L,"");
        r.stage("BAT.AFTER",lines("name","durability 99"),16_016_000_000L,"");
        check(log.subList(before,log.size()).stream().anyMatch(s->s.contains("firstMutationOwner=BAT.AFTER")&&s.contains("index=1")),"Unchanged input / changed output identifies BAT and exact line");
        log.clear();r=new Recorder(log::add);
        for(int frame=1;frame<=2;frame++) {
            r.begin(frame,"same-item");
            r.stage("BAT.BEFORE",lines(frame==1?"one":"two"),frame,"");
            r.stage("BAT.AFTER",lines(frame==1?"one":"two"),frame,"");
        }
        check(log.stream().anyMatch(s->s.contains("firstMutationOwner=UNOBSERVED_INTERVAL_BEFORE:BAT.BEFORE")),"Changed handler input must not blame BAT");
        check(log.stream().filter(s->s.contains("FIRST-DIVERGENCE")).count()==1,"One first observed mutation per frame");
        log.clear();r.begin(8,"same-item");r.stage("BAT.BEFORE",lines("two"),8,"");
        check(!log.isEmpty()&&log.stream().noneMatch(s->s.contains("FIRST-DIVERGENCE")),"Hover reentry emits fresh snapshot without stale comparison");
        log.clear();r.begin(9,"different-count-or-components");r.stage("BAT.BEFORE",lines("x"),9,"");
        check(log.stream().noneMatch(s->s.contains("FIRST-DIVERGENCE")),"Logical item change resets comparison");
        log.clear();r=new Recorder(log::add);
        for(int frame=1;frame<=100;frame++) {
            r.begin(frame,"same");
            r.stage("prepare",lines("a"),frame,"");r.stage("prepare",lines("b"),frame,"");
            r.pair("BAT",frame,lines("a"),lines("b"),frame,"");r.pair("BAT",frame,lines("b"),lines("c"),frame,"");
        }
        check(log.size()==4,"Repeated distinct occurrences must not alternate a single dedupe key");
        var plain=TooltipTrace.element(Component.literal("Pickaxe"));
        var copy=TooltipTrace.element(Component.literal("Pickaxe"));
        var styled=TooltipTrace.element(Component.literal("Pickaxe").withStyle(Style.EMPTY.withBold(true)));
        check(plain.hash().equals(copy.hash()),"New Component objects with equal visible content remain stable");
        check(!plain.hash().equals(styled.hash()),"Style-only mutation detected");
        check(TooltipTrace.element(Either.left(Component.literal("Pickaxe"))).hash().equals(plain.hash()),"JEI Either unwrapped");
        class ClientText implements ClientTextTooltipBridge { public FormattedCharSequence ctbn$getText(){return FormattedCharSequence.forward("Pickaxe",Style.EMPTY);} }
        check(TooltipTrace.element(new ClientText()).text().equals(plain.text()),"Rendered text uses readable styled contents");
        check(TooltipTrace.element(new Object()).coverage().equals("opaque-type-only"),"Unknown visual content is explicitly partial, never object identity");
        check(TooltipTrace.element(new Object()).hash().equals(TooltipTrace.element(new Object()).hash()),"Opaque object allocation cannot create false content changes");
        check(plain.coverage().equals("full-component-json-and-styled-text"),"Full component serialization is available for text");
        check(!TooltipTrace.element(Component.literal("Pickaxe").withStyle(Style.EMPTY.withInsertion("different"))).hash().equals(plain.hash()),"Non-visible style insertion changes full component fingerprint");
        log.clear();r=new Recorder(log::add);
        for(int f=1;f<=500;f++) {
            long now=f*16_000_000L;r.begin(f,"stable-pickaxe",now);
            Snapshot unstable=lines("Obsidian Breaker III", "styled-change-"+f);
            r.chain("grid");r.stage("ITEMSTACK-TOOLTIP-GENERATED",unstable,now,"");
            r.stage("ITEMTOOLTIPEVENT-PRE-POST",unstable,now,"");
            r.stage("BAT.BEFORE",unstable,now,"verifiedDispatch=true");r.stage("BAT.AFTER",unstable,now,"verifiedDispatch=true");r.pair("BAT",f,unstable,unstable,now,"");
            r.stage("ICON.BEFORE",unstable,now,"verifiedDispatch=true");r.stage("ICON.AFTER",unstable,now,"verifiedDispatch=true");r.pair("ICON",f,unstable,unstable,now,"");
            r.stage("JEI",unstable,now,"");r.stage("GuiGraphics",unstable,now,"");r.chain("ColorTooltips inherited");
        }
        check(log.size()<600,"500 unstable frames should emit mostly earliest divergence, got "+log.size());
        check(log.stream().filter(s->s.contains("changed=false BEFORE[")).count()==2,"Non-mutating handlers proved once despite changing inputs");
        check(log.stream().filter(s->s.contains("TOOLTIP-FIRST-DIVERGENCE")).allMatch(s->s.contains("firstMutationOwner=ITEMSTACK_TOOLTIP_GENERATION")),"Raw-generation divergence narrows pre-BAT region");
        int marker=log.size();long now=501*16_000_000L;r.begin(501,"stable-pickaxe",now);
        Snapshot raw=lines("Obsidian Breaker III","styled-change-500"),mutated=lines("Obsidian Breaker III","earlier-listener-change");
        r.stage("ITEMSTACK-TOOLTIP-GENERATED",raw,now,"");r.stage("ITEMTOOLTIPEVENT-PRE-POST",raw,now,"");
        r.stage("real.Handler.BEFORE",raw,now,"verifiedDispatch=true");
        // First occurrence of a new handler establishes a baseline, then its next-frame change attributes it.
        r.stage("real.Handler.AFTER",raw,now,"verifiedDispatch=true");
        now=502*16_000_000L;r.begin(502,"stable-pickaxe",now);
        r.stage("ITEMSTACK-TOOLTIP-GENERATED",raw,now,"");r.stage("ITEMTOOLTIPEVENT-PRE-POST",raw,now,"");
        r.stage("real.Handler.BEFORE",raw,now,"verifiedDispatch=true");r.stage("real.Handler.AFTER",mutated,now,"verifiedDispatch=true");
        check(log.subList(marker,log.size()).stream().anyMatch(s->s.contains("firstMutationOwner=real.Handler.AFTER")),"Verified handler after point narrows dispatch mutation");
        check(log.subList(marker,log.size()).stream().anyMatch(s->s.contains("TOOLTIP-TRACE path=ITEMSTACK-TOOLTIP-GENERATED")),"Changed earliest owner flushes earlier buffered chain");
        marker=log.size();r.pair("BAT",502,raw,mutated,now,"verifiedDispatch=true");
        check(log.subList(marker,log.size()).stream().anyMatch(s->s.contains("owner=BAT")&&s.contains("changed=true")),"Previously non-mutating BAT remains monitored");
        log.clear();r=new Recorder(log::add);
        for(int f=1;f<=2;f++) {
            r.begin(f,"stable",f);
            r.stage("ITEMSTACK-TOOLTIP-GENERATED",lines("raw"),f,"");r.stage("ITEMTOOLTIPEVENT-PRE-POST",lines("raw"),f,"");
            r.stage("BAT.BEFORE",lines(f==1?"old":"new"),f,"verifiedDispatch=true");
        }
        check(log.stream().anyMatch(s->s.contains("firstMutationOwner=EVENTBUS_DISPATCH_BEFORE:BAT.BEFORE")),"Untraced earlier listener is an honest dispatch interval, not invented owner");
        var serializedDiff=TraceModel.diff(Snapshot.of(List.of(plain)),Snapshot.of(List.of(styled)));
        check(serializedDiff.contains("representationFirstDifference="),"Style serialization difference includes a concrete excerpt");
        TooltipTrace.beginFrame(1,new Object(),net.minecraft.core.RegistryAccess.EMPTY);
        var item=new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_PICKAXE);
        TooltipTrace.hover(item,10,10);
        check(TooltipTrace.matchesHover(item.copy()),"Copied ItemStack remains the same logical hover");
        var differentCount=item.copyWithCount(2);
        check(!TooltipTrace.matchesHover(differentCount),"Count affects logical hover");
        var differentComponents=item.copy();differentComponents.setDamageValue(4);
        check(!TooltipTrace.matchesHover(differentComponents),"Actual component changes affect logical hover");
        var eventLines=new ArrayList<Component>(List.of(Component.literal("Obsidian Breaker III")));
        UpstreamTrace.itemEntry(item);UpstreamTrace.generated(item,eventLines);
        var event=new net.neoforged.neoforge.event.entity.player.ItemTooltipEvent(item,null,eventLines,net.minecraft.world.item.TooltipFlag.NORMAL,net.minecraft.world.item.Item.TooltipContext.EMPTY);
        UpstreamTrace.created(event);
        check(!UpstreamTrace.dispatched(event),"Construction alone is not bus dispatch");
        UpstreamTrace.prePost(item,eventLines);
        check(UpstreamTrace.dispatched(event),"Exact captured event recognized inside post boundary");
        var unrelated=new net.neoforged.neoforge.event.entity.player.ItemTooltipEvent(item,null,eventLines,net.minecraft.world.item.TooltipFlag.NORMAL,net.minecraft.world.item.Item.TooltipContext.EMPTY);
        check(!UpstreamTrace.dispatched(unrelated),"Different event cannot be attributed to captured dispatch");
        check(UpstreamTrace.handlerEvidence("real.FirstHandler",event).contains("tracedHandlerOrder=1"),"Actual traced invocation order");
        check(UpstreamTrace.handlerEvidence("BAT",event).contains("tracedHandlerOrder=2"),"BAT is not assumed first");
        UpstreamTrace.postPost(item,eventLines);
        check(!UpstreamTrace.dispatched(event),"Post-return ends dispatch interval");
        UpstreamTrace.returned(item,eventLines);UpstreamTrace.itemReturn(item);
        check(eventLines.size()==1&&eventLines.getFirst().getString().equals("Obsidian Breaker III"),"Boundary observers leave actual tooltip list untouched");
        System.out.println("PASS "+checks+" diagnostic checks; stable/unstable frame-volume, upstream attribution, full serialization, real ItemStack equality, and exact event-scope tests.");
    }
}
