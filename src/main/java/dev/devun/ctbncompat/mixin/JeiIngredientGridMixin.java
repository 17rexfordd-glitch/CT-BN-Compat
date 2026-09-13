package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import java.util.Optional;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.gui.overlay.elements.IElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "mezz.jei.gui.overlay.ingredients.IngredientGrid", remap = false)
public abstract class JeiIngredientGridMixin {
    @Inject(
            method = "drawTooltips(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;II)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private void ctbn$jeiGridDrawTooltips(Minecraft minecraft, GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        DiagnosticLog.jeiGridDrawTooltips(mouseX, mouseY, "IngredientGrid.drawTooltips");
    }

    @Inject(
            method = "drawTooltip(Lnet/minecraft/client/gui/GuiGraphics;IILmezz/jei/gui/overlay/elements/IElement;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private <T> void ctbn$jeiGridDrawTooltip(GuiGraphics graphics, int mouseX, int mouseY, IElement<T> element, CallbackInfo ci) {
        ITypedIngredient<T> typedIngredient = element == null ? null : element.getTypedIngredient();
        Object ingredient = typedIngredient == null ? null : typedIngredient.getIngredient();
        Object ingredientType = typedIngredient == null ? null : typedIngredient.getType();
        ItemStack stack = ctbn$extractItemStack(typedIngredient, ingredient);
        DiagnosticLog.jeiIngredientHover(element, typedIngredient, ingredient, ingredientType, stack, mouseX, mouseY, "IngredientGrid.drawTooltip");
    }

    private static <T> ItemStack ctbn$extractItemStack(ITypedIngredient<T> typedIngredient, Object ingredient) {
        if (typedIngredient != null) {
            Optional<ItemStack> optionalStack = typedIngredient.getItemStack();
            if (optionalStack.isPresent()) {
                ItemStack stack = optionalStack.get();
                return stack == null || stack.isEmpty() ? null : stack;
            }
        }
        if (ingredient instanceof ItemStack stack) {
            return stack.isEmpty() ? null : stack;
        }
        return null;
    }
}
