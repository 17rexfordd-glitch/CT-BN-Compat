package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import dev.devun.ctbncompat.TooltipTrace;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.pixeldreamstudios.iconleadingtooltip.neoforge.client.IconLeadingTooltipNeoForgeClient", remap = false)
public abstract class IconLeadingTooltipMixin {
    @Inject(
            method = "onItemTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$iconTooltip(ItemTooltipEvent event, CallbackInfo ci) {
        DiagnosticLog.hookExecuted("ICON_LEADING_TOOLTIP", "IconLeadingTooltipNeoForgeClient.onItemTooltip");
        DiagnosticLog.owner("ICON_LEADING_TOOLTIP");
        TooltipTrace.before("ICON_LEADING_TOOLTIP", event);
    }
    @Inject(method = "onItemTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",
            at = @At("RETURN"), require = 1, remap = false)
    private static void ctbn$iconTooltipAfter(ItemTooltipEvent event, CallbackInfo ci) {
        TooltipTrace.after("ICON_LEADING_TOOLTIP", event);
    }
}
