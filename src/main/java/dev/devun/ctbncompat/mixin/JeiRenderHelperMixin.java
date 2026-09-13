package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "mezz.jei.neoforge.platform.RenderHelper", remap = false)
public abstract class JeiRenderHelperMixin {
    @Inject(
            method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/List;IILnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderComponentTooltipFromElements(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/world/item/ItemStack;)V"
            ),
            require = 1,
            remap = false
    )
    private void ctbn$jeiRenderTooltipHandoff(GuiGraphics graphics, List<?> tooltipElements, int mouseX, int mouseY, Font font, ItemStack stack, CallbackInfo ci) {
        DiagnosticLog.jeiRenderHandoff(stack, tooltipElements, mouseX, mouseY, "RenderHelper.renderTooltip");
    }
}
