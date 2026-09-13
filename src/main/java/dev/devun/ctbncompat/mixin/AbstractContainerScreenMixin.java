package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import dev.devun.ctbncompat.bridge.AbstractContainerScreenBridge;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements AbstractContainerScreenBridge {
    @Shadow protected Slot hoveredSlot;

    @Override
    public Slot ctbn$getHoveredSlot() {
        return hoveredSlot;
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("RETURN"), require = 1)
    private void ctbn$liveContainerFrame(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        DiagnosticLog.liveContainerFrame(this, hoveredSlot, mouseX, mouseY);
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("HEAD"), require = 1)
    private void ctbn$tooltipBefore(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        DiagnosticLog.liveTooltipCall("BEFORE", this, hoveredSlot, mouseX, mouseY);
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("RETURN"), require = 1)
    private void ctbn$tooltipAfter(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        DiagnosticLog.liveTooltipCall("AFTER", this, hoveredSlot, mouseX, mouseY);
    }
}
