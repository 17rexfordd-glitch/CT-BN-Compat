package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenRenderMixin {
    @Inject(method = "renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"), require = 1)
    private void ctbn$frame(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        DiagnosticLog.beginFrame(this, mouseX, mouseY);
        DiagnosticLog.screenHoveredProbe(this, mouseX, mouseY);
    }
}
