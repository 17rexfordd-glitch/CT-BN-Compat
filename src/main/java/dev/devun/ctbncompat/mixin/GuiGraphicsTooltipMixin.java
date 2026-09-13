package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsTooltipMixin {
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"), require = 1)
    private void ctbn$item(Font font, ItemStack stack, int x, int y, CallbackInfo ci) {
        DiagnosticLog.tooltipBegin("renderTooltip(ItemStack)", stack, null, x, y);
    }

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"), require = 1)
    private void ctbn$internal(Font font, List<?> tooltip, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        DiagnosticLog.tooltipInternal(tooltip, x, y, positioner);
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), require = 1)
    private void ctbn$components(Font font, List<?> tooltip, Optional<?> component, int x, int y, CallbackInfo ci) {
        DiagnosticLog.tooltipBegin("renderTooltip(List,Optional)", null, tooltip, x, y);
    }

    @Inject(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At("HEAD"), require = 1)
    private void ctbn$component(Font font, List<?> tooltip, int x, int y, CallbackInfo ci) {
        DiagnosticLog.tooltipBegin("renderComponentTooltip(List)", null, tooltip, x, y);
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At("HEAD"), require = 1)
    private void ctbn$ordered(Font font, List<?> tooltip, int x, int y, CallbackInfo ci) {
        DiagnosticLog.tooltipBegin("renderTooltip(List)", null, tooltip, x, y);
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;II)V", at = @At("HEAD"), require = 1)
    private void ctbn$positioned(Font font, List<?> tooltip, ClientTooltipPositioner positioner, int x, int y, CallbackInfo ci) {
        DiagnosticLog.tooltipBegin("renderTooltip(List,Positioner)", null, tooltip, x, y);
    }
}
