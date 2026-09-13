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
@Mixin(targets="top.theillusivec4.curios.client.ClientEventHandler",remap=false)
public abstract class UpstreamSubscriber15Mixin {
    @Inject(method="onTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",at=@At("HEAD"),require=1,remap=false)
    private void ctbn$before(ItemTooltipEvent event,CallbackInfo ci) { UpstreamTrace.before("top.theillusivec4.curios.client.ClientEventHandler.onTooltip",event); }
    @Inject(method="onTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",at=@At("RETURN"),require=1,remap=false)
    private void ctbn$after(ItemTooltipEvent event,CallbackInfo ci) { UpstreamTrace.after("top.theillusivec4.curios.client.ClientEventHandler.onTooltip",event); }
}
