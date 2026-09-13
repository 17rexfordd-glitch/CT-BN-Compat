package dev.devun.ctbncompat.mixin;
import dev.devun.ctbncompat.TooltipTrace;
import dev.devun.ctbncompat.UpstreamTrace;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(targets="net.bettercombat.neoforge.client.NeoForgeClientEvents",remap=false)
public abstract class UpstreamSubscriber10Mixin {
    @Inject(method="onTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",at=@At("HEAD"),require=1,remap=false)
    private static void ctbn$before(ItemTooltipEvent event,CallbackInfo ci) { UpstreamTrace.before("net.bettercombat.neoforge.client.NeoForgeClientEvents.onTooltip",event); }
    @Inject(method="onTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",at=@At("RETURN"),require=1,remap=false)
    private static void ctbn$after(ItemTooltipEvent event,CallbackInfo ci) { UpstreamTrace.after("net.bettercombat.neoforge.client.NeoForgeClientEvents.onTooltip",event); }
}
