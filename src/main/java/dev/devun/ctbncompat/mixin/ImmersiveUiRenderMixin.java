package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "it.hurts.octostudios.immersiveui.util.CommonCode", remap = false)
public abstract class ImmersiveUiRenderMixin {
    @Inject(
            method = "renderFloating(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/gui/GuiGraphics;Lit/hurts/octostudios/immersiveui/client/MouseInfo;IILnet/minecraft/world/item/ItemStack;Ljava/util/Random;Lit/hurts/octostudios/immersiveui/client/RenderInfo;Ljava/lang/String;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$immersive(CallbackInfo ci) {
        DiagnosticLog.hookExecuted("IMMERSIVEUI", "CommonCode.renderFloating");
        DiagnosticLog.owner("IMMERSIVEUI");
    }
}
