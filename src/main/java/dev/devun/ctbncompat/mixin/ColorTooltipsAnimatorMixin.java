package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.yanbwe.colortooltips.animation.TooltipTarget;

@Mixin(targets = "org.yanbwe.colortooltips.animation.TooltipAnimator", remap = false)
public abstract class ColorTooltipsAnimatorMixin {
    @Inject(method = "reset()V", at = @At("HEAD"), require = 1, remap = false)
    private void ctbn$reset(CallbackInfo ci) {
        DiagnosticLog.animator("reset");
    }

    @Inject(method = "restartTransition()V", at = @At("HEAD"), require = 1, remap = false)
    private void ctbn$restart(CallbackInfo ci) {
        DiagnosticLog.animator("restartTransition");
    }

    @Inject(method = "resetAlpha()V", at = @At("HEAD"), require = 1, remap = false)
    private void ctbn$alpha(CallbackInfo ci) {
        DiagnosticLog.animator("resetAlpha");
    }

    @Inject(method = "resetAlphaToZero()V", at = @At("HEAD"), require = 1, remap = false)
    private void ctbn$alpha0(CallbackInfo ci) {
        DiagnosticLog.animator("resetAlphaToZero");
    }

    @Inject(
            method = "forceInit(Lorg/yanbwe/colortooltips/animation/TooltipTarget;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private void ctbn$force(TooltipTarget target, CallbackInfo ci) {
        DiagnosticLog.animator("forceInit");
    }
}
