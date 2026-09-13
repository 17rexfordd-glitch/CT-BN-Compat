package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.sweenus.simplytooltips.client.render.TooltipRenderer", remap = false)
public abstract class SimplyTooltipsRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Lnet/sweenus/simplytooltips/api/TooltipProvider;IIII)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$simplyRenderPrimary(CallbackInfo ci) {
        DiagnosticLog.hookExecuted("SIMPLYTOOLTIPS", "TooltipRenderer.render(primary)");
        DiagnosticLog.owner("SIMPLYTOOLTIPS");
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Lnet/sweenus/simplytooltips/api/TooltipProvider;Ljava/util/List;IIII)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$simplyRenderNativeComponents(CallbackInfo ci) {
        DiagnosticLog.hookExecuted("SIMPLYTOOLTIPS", "TooltipRenderer.render(native-components)");
        DiagnosticLog.owner("SIMPLYTOOLTIPS");
    }
}
