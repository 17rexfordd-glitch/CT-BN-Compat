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
@Mixin(targets="net.neoforged.neoforge.event.EventHooks", remap=false)
public abstract class EventHooksTooltipMixin {
    @Inject(method="onItemTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/world/item/Item$TooltipContext;)Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;", at=@At("HEAD"), require=1, remap=false)
    private static void ctbn$generated(ItemStack stack, Player player, List<Component> list, TooltipFlag flag, Item.TooltipContext context, CallbackInfoReturnable<ItemTooltipEvent> cir) { UpstreamTrace.generated(stack,list); }
    @Inject(method="onItemTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/world/item/Item$TooltipContext;)Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;", at=@At(value="INVOKE",target="Lnet/neoforged/bus/api/IEventBus;post(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;",shift=At.Shift.BEFORE), require=1, remap=false)
    private static void ctbn$prePost(ItemStack stack, Player player, List<Component> list, TooltipFlag flag, Item.TooltipContext context, CallbackInfoReturnable<ItemTooltipEvent> cir) { UpstreamTrace.prePost(stack,list); }
    @Inject(method="onItemTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/world/item/Item$TooltipContext;)Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;", at=@At(value="INVOKE",target="Lnet/neoforged/bus/api/IEventBus;post(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;",shift=At.Shift.AFTER), require=1, remap=false)
    private static void ctbn$postPost(ItemStack stack, Player player, List<Component> list, TooltipFlag flag, Item.TooltipContext context, CallbackInfoReturnable<ItemTooltipEvent> cir) { UpstreamTrace.postPost(stack,list); }
    @Inject(method="onItemTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/world/item/Item$TooltipContext;)Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;", at=@At("RETURN"), require=1, remap=false)
    private static void ctbn$returned(ItemStack stack, Player player, List<Component> list, TooltipFlag flag, Item.TooltipContext context, CallbackInfoReturnable<ItemTooltipEvent> cir) { UpstreamTrace.returned(stack,list); }
}
