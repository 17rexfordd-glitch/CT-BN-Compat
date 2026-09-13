package dev.devun.ctbncompat;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ColorTooltipsBetterNetherCompat.MOD_ID, dist = Dist.CLIENT)
public final class ColorTooltipsBetterNetherCompat {
    public static final String MOD_ID = "colortooltips_immersiverpg_compat";

    public ColorTooltipsBetterNetherCompat() {
        NeoForge.EVENT_BUS.addListener(ClientRuntimeDiagnostics::onClientTick);
        DiagnosticLog.log("v2.2.2 diagnostics ACTIVE clean-baseline=true neoforge-event-bus-descriptor=IEventBus minecraft-slots-descriptor=NonNullList tooltip-owner-dedupe=true colortooltips-no-live-item-dedupe=true runtime-hover-dedupe=true tooltip-mutation-tracing=true logical-state-dedupe=true behavioral-fixes=false");
        DiagnosticLog.log("CLIENT-RUNTIME-ANCHOR REGISTERED event=net.neoforged.neoforge.client.event.ClientTickEvent.Post bus=NeoForge.EVENT_BUS descriptor=Lnet/neoforged/bus/api/IEventBus;");
        DiagnosticLog.applied(DiagnosticMixinPlugin.appliedSnapshot());
        DiagnosticLog.verifiedPrimaryHooks();
    }
}
