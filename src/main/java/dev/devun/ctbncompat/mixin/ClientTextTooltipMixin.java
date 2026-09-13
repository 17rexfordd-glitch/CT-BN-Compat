package dev.devun.ctbncompat.mixin;
import dev.devun.ctbncompat.bridge.ClientTextTooltipBridge;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
@Mixin(ClientTextTooltip.class)
public abstract class ClientTextTooltipMixin implements ClientTextTooltipBridge {
    @Shadow @Final private FormattedCharSequence text;
    public FormattedCharSequence ctbn$getText() { return text; }
}
