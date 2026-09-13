package dev.devun.ctbncompat.mixin;
import dev.devun.ctbncompat.TooltipTrace;
import dev.devun.ctbncompat.UpstreamTrace;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {
    @Inject(method="getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at=@At("HEAD"), require=1)
    private void ctbn$entry(Item.TooltipContext context,Player player,TooltipFlag flag,CallbackInfoReturnable<List<Component>> cir) {
        UpstreamTrace.itemEntry((ItemStack)(Object)this);
    }
    @Inject(method="getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at=@At("RETURN"), require=1)
    private void ctbn$return(Item.TooltipContext context,Player player,TooltipFlag flag,CallbackInfoReturnable<List<Component>> cir) {
        UpstreamTrace.itemReturn((ItemStack)(Object)this);
    }
}
