package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.yanbwe.colortooltips.animation.TooltipLifecycleEventType;

@Mixin(targets = "org.yanbwe.colortooltips.animation.TooltipLifecycleEventBus", remap = false)
public abstract class ColorTooltipsLifecycleMixin {
    @Inject(
            method = "publish(Lorg/yanbwe/colortooltips/animation/TooltipLifecycleEventType;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private static void ctbn$publish(TooltipLifecycleEventType type, CallbackInfo ci) {
        DiagnosticLog.lifecycle(type);
    }
}
