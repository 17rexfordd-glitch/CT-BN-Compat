package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import dev.devun.ctbncompat.TooltipTrace;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.latvian.mods.betteradvancedtooltips.BATClientEventHandler", remap = false)
public abstract class BetterAdvancedTooltipsMixin {
    @Inject(
            method = "onItemTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$bat(ItemTooltipEvent event, CallbackInfo ci) {
        DiagnosticLog.hookExecuted("BETTER_ADVANCED_TOOLTIPS", "BATClientEventHandler.onItemTooltip");
        DiagnosticLog.owner("BETTER_ADVANCED_TOOLTIPS");
        TooltipTrace.before("BETTER_ADVANCED_TOOLTIPS", event);
    }
    @Inject(method = "onItemTooltip(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V",
            at = @At("RETURN"), require = 1, remap = false)
    private static void ctbn$batAfter(ItemTooltipEvent event, CallbackInfo ci) {
        TooltipTrace.after("BETTER_ADVANCED_TOOLTIPS", event);
    }
}
