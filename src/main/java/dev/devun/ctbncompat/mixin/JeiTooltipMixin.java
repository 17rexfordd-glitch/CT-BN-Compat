package dev.devun.ctbncompat.mixin;

import dev.devun.ctbncompat.DiagnosticLog;
import dev.devun.ctbncompat.TooltipTrace;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.gui.JeiTooltip;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "mezz.jei.common.gui.JeiTooltip", remap = false)
public abstract class JeiTooltipMixin {
    @Inject(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphics;II)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private void ctbn$jeiDrawLegacy(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        DiagnosticLog.hookExecuted("JEI", "JeiTooltip.draw(GuiGraphics,int,int)");
        DiagnosticLog.owner("JEI");
    }

    @Inject(
            method = "prepareForIngredientTooltip(Lmezz/jei/api/ingredients/ITypedIngredient;Lmezz/jei/api/ingredients/IIngredientRenderer;Lmezz/jei/api/runtime/IIngredientManager;)Lmezz/jei/common/gui/JeiTooltip$TooltipRenderData;",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private <T> void ctbn$jeiPrepareForIngredientTooltip(ITypedIngredient<T> typedIngredient, IIngredientRenderer<T> renderer, IIngredientManager ingredientManager, CallbackInfoReturnable<?> cir) {
        Object ingredient = typedIngredient == null ? null : typedIngredient.getIngredient();
        Object ingredientType = typedIngredient == null ? null : typedIngredient.getType();
        ItemStack stack = ctbn$extractItemStack(typedIngredient, ingredient);
        DiagnosticLog.jeiTooltipPrepare(stack, typedIngredient, ingredient, ingredientType, renderer, ingredientManager, "JeiTooltip.prepareForIngredientTooltip");
        TooltipTrace.point("JeiTooltip.prepareForIngredientTooltip.BEFORE", stack, ((JeiTooltip)(Object)this).getLines());
    }

    @Inject(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphics;IILmezz/jei/api/ingredients/ITypedIngredient;Lmezz/jei/api/ingredients/IIngredientRenderer;Lmezz/jei/api/runtime/IIngredientManager;)V",
            at = @At("HEAD"),
            require = 1,
            remap = false
    )
    private <T> void ctbn$jeiDrawIngredient(GuiGraphics graphics, int mouseX, int mouseY, ITypedIngredient<T> typedIngredient, IIngredientRenderer<T> renderer, IIngredientManager ingredientManager, CallbackInfo ci) {
        Object ingredient = typedIngredient == null ? null : typedIngredient.getIngredient();
        Object ingredientType = typedIngredient == null ? null : typedIngredient.getType();
        ItemStack stack = ctbn$extractItemStack(typedIngredient, ingredient);
        List<?> lines = ((JeiTooltip) (Object) this).getLines();
        DiagnosticLog.jeiTooltipDraw(this, lines, stack, mouseX, mouseY, typedIngredient, ingredient, ingredientType, renderer, ingredientManager, "JeiTooltip.draw(typedIngredient)");
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
    @Inject(method = "prepareForIngredientTooltip(Lmezz/jei/api/ingredients/ITypedIngredient;Lmezz/jei/api/ingredients/IIngredientRenderer;Lmezz/jei/api/runtime/IIngredientManager;)Lmezz/jei/common/gui/JeiTooltip$TooltipRenderData;",
            at = @At("RETURN"), require = 1, remap = false)
    private <T> void ctbn$jeiPrepareAfter(ITypedIngredient<T> typedIngredient, IIngredientRenderer<T> renderer, IIngredientManager manager, CallbackInfoReturnable<?> cir) {
        Object ingredient = typedIngredient == null ? null : typedIngredient.getIngredient();
        TooltipTrace.point("JeiTooltip.prepareForIngredientTooltip.AFTER", ctbn$extractItemStack(typedIngredient, ingredient), ((JeiTooltip)(Object)this).getLines());
    }
}
