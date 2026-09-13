package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.yanbwe.colortooltips.animation.TooltipState;
import org.yanbwe.colortooltips.animation.TooltipTarget;

@Mixin(targets = "org.yanbwe.colortooltips.animation.TooltipAnimationSystem", remap = false)
public abstract class ColorTooltipsAnimationMixin {
    @Shadow(remap = false) private static ItemStack activeStack;
    @Shadow(remap = false) private static boolean visibleRequested;
    @Shadow(remap = false) private static boolean isTextOnlyMode;
    @Shadow(remap = false) private static boolean needsAnimatorReset;
    @Shadow(remap = false) private static long lostCandidateStartTimeMs;
    @Shadow(remap = false) private static long switchFlashStartTimeMs;
    @Shadow(remap = false) private static long fadeInStartTimeMs;
    @Shadow(remap = false) private static TooltipState lastState;

    private static void ctbn$capture() {
        DiagnosticLog.captureColorTooltipsState(
                activeStack,
                visibleRequested,
                isTextOnlyMode,
                needsAnimatorReset,
                lostCandidateStartTimeMs,
                switchFlashStartTimeMs,
                fadeInStartTimeMs,
                lastState
        );
    }

    @Inject(
            method = "onLiveItemObserved(Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$live(ItemStack stack, CallbackInfo ci) {
        ctbn$capture();
        DiagnosticLog.ctObserved("onLiveItemObserved", stack);
    }

    @Inject(
            method = "setNeedsAnimatorReset(Z)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$resetReq(boolean value, CallbackInfo ci) {
        ctbn$capture();
        DiagnosticLog.ctState("setNeedsAnimatorReset(" + value + ")");
    }

    @Inject(method = "reset()V", at = @At("HEAD"), require = 1, remap = false)
    private static void ctbn$reset(CallbackInfo ci) {
        ctbn$capture();
        DiagnosticLog.ctState("reset()");
    }

    @Inject(method = "onFrameWithoutLiveItem()V", at = @At("HEAD"), require = 1, remap = false)
    private static void ctbn$lost(CallbackInfo ci) {
        ctbn$capture();
        DiagnosticLog.ctState("onFrameWithoutLiveItem()");
    }

    @Inject(
            method = "update(Lnet/minecraft/world/item/ItemStack;Lorg/yanbwe/colortooltips/animation/TooltipTarget;)Lorg/yanbwe/colortooltips/animation/TooltipState;",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$update(ItemStack stack, TooltipTarget target, CallbackInfoReturnable<TooltipState> cir) {
        ctbn$capture();
        DiagnosticLog.ctObserved("update()", stack);
    }
}
