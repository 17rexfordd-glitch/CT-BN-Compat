package dev.devun.ctbncompat;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class DiagnosticMixinPlugin implements IMixinConfigPlugin {
    private static final Map<String,String[]> UPSTREAM_CANDIDATES = Map.ofEntries(
            Map.entry("UpstreamSubscriber01Mixin", new String[]{"com/github/alexthe666/alexsmobs/event/ServerEvents","onTooltip","alexsmobs-1.22.17.jar","8"}),
            Map.entry("UpstreamSubscriber02Mixin", new String[]{"net/mehvahdjukaar/amendments/platform/ClientEvents","tooltipEvent","amendments-neoforge-1.21-2.1.7.jar","8"}),
            Map.entry("UpstreamSubscriber03Mixin", new String[]{"dev/shadowsoffire/apotheosis/client/AdventureModuleClient","affixTooltips","Apotheosis-1.21.1-8.7.0.jar","8"}),
            Map.entry("UpstreamSubscriber04Mixin", new String[]{"dev/shadowsoffire/apotheosis/client/AdventureModuleClient","showBlacklistedPotions","Apotheosis-1.21.1-8.7.0.jar","8"}),
            Map.entry("UpstreamSubscriber05Mixin", new String[]{"dev/shadowsoffire/apothic_attributes/client/AttributesLibClient","potionTooltips","ApothicAttributes-1.21.1-2.10.1.jar","0"}),
            Map.entry("UpstreamSubscriber06Mixin", new String[]{"dev/muon/apothiccombat/TooltipHandler","onTooltip","apothiccombat-1.2.1.jar","8"}),
            Map.entry("UpstreamSubscriber07Mixin", new String[]{"dev/shadowsoffire/apothic_enchanting/ApothEnchClient$ForgeBusEvents","tooltips","ApothicEnchanting-1.21.1-1.6.1.jar","0"}),
            Map.entry("UpstreamSubscriber08Mixin", new String[]{"dev/shadowsoffire/apothic_spawners/ASEvents","handleTooltips","ApothicSpawners-1.21.1-1.4.0.jar","0"}),
            Map.entry("UpstreamSubscriber09Mixin", new String[]{"dev/architectury/event/forge/EventHandlerImplClient","event","architectury-13.0.11-neoforge.jar","8"}),
            Map.entry("UpstreamSubscriber10Mixin", new String[]{"net/bettercombat/neoforge/client/NeoForgeClientEvents","onTooltip","bettercombat-neoforge-2.4.0+1.21.1.jar","8"}),
            Map.entry("UpstreamSubscriber11Mixin", new String[]{"com/arxyt/colonypathingedition/core/event/ItemTooltipEvent","onItemTooltipEvent","colonypathingedition-1.21.1-1.0.5-ALPHA-12-fix-1.jar","8"}),
            Map.entry("UpstreamSubscriber12Mixin", new String[]{"com/simibubi/create/foundation/events/ClientEvents","addToItemTooltip","create-1.21.1-6.0.10.jar","8"}),
            Map.entry("UpstreamSubscriber13Mixin", new String[]{"net/mcreator/createstuffadditions/procedures/DisableEnchantementTooltipProcedure","onItemTooltip","create-stuff-additions1.21.1_v2.1.4b.jar","8"}),
            Map.entry("UpstreamSubscriber14Mixin", new String[]{"dev/averageanime/neoforge/item/TooltipEvents","onItemTooltip","createfood-neoforge-1.21.1-2.7.1.jar","8"}),
            Map.entry("UpstreamSubscriber15Mixin", new String[]{"top/theillusivec4/curios/client/ClientEventHandler","onTooltip","curios-neoforge-9.5.1+1.21.1.jar","0"}),
            Map.entry("UpstreamSubscriber16Mixin", new String[]{"com/dtteam/dynamictrees/event/handler/ClientGameEventHandler","onItemTooltipAdded","dynamictrees-neoforge-1.21.1-1.7.2.jar","8"}),
            Map.entry("UpstreamSubscriber17Mixin", new String[]{"vectorwing/farmersdelight/client/event/TooltipEvents","addTooltipToVanillaSoups","FarmersDelight-1.21.1-1.3.3.jar","8"}),
            Map.entry("UpstreamSubscriber18Mixin", new String[]{"com/anthonyhilyard/iceberg/neoforge/client/IcebergNeoForgeClient$NeoForgeEvents","itemTooltipEvent","Iceberg-1.21.1-neoforge-1.3.2.jar","8"}),
            Map.entry("UpstreamSubscriber19Mixin", new String[]{"immersive_aircraft/neoforge/NeoForgeBusEvents","onItemTooltips","immersive_aircraft-1.4.6+1.21.1-neoforge.jar","8"}),
            Map.entry("UpstreamSubscriber20Mixin", new String[]{"dev/latvian/mods/kubejs/client/KubeJSClientEventHandler","onItemTooltip","kubejs-neoforge-2101.7.2-build.374.jar","8"}),
            Map.entry("UpstreamSubscriber21Mixin", new String[]{"com/minecolonies/core/event/ClientEventHandler","onItemTooltipEvent","minecolonies-1.1.1345-1.21.1-snapshot.jar","8"}),
            Map.entry("UpstreamSubscriber22Mixin", new String[]{"org/yanbwe/raritycore/client/RarityTooltipHandler","onItemTooltip","raritycore-1211.14.5.jar","8"}),
            Map.entry("UpstreamSubscriber23Mixin", new String[]{"it/hurts/sskirillss/relics/client/handlers/DescriptionHandler","onItemTooltip","relics-1.21.1-0.12.8.jar","8"}),
            Map.entry("UpstreamSubscriber24Mixin", new String[]{"it/hurts/shatterbyte/reliquified_artifacts/handlers/TooltipHandler","onTooltip","reliquified_artifacts-1.21.1-1.0.8.jar","8"}),
            Map.entry("UpstreamSubscriber25Mixin", new String[]{"cy/jdkdigital/sgearmetalworks/event/EventHandler","itemTooltip","sgearmetalworks-1.21.1-1.5.0.jar","8"}),
            Map.entry("UpstreamSubscriber26Mixin", new String[]{"net/silentchaos512/gear/client/event/TooltipHandler","onTooltip","silent-gear-1.21.1-neoforge-4.2.1.1.jar","0"}),
            Map.entry("UpstreamSubscriber27Mixin", new String[]{"net/silentchaos512/gear/setup/SgDataComponents$TooltipHandler","onTooltip","silent-gear-1.21.1-neoforge-4.2.1.1.jar","8"}),
            Map.entry("UpstreamSubscriber28Mixin", new String[]{"com/cazsius/solcarrot/client/TooltipHandler","onItemTooltip","solcarrot-1.21.1-1.16.6.jar","8"}),
            Map.entry("UpstreamSubscriber29Mixin", new String[]{"net/mehvahdjukaar/supplementaries/common/events/platform/ClientEventsForge","onItemTooltip","supplementaries-1.21.1-3.9.1-neoforge.jar","8"}),
            Map.entry("UpstreamSubscriber30Mixin", new String[]{"ovh/corail/tombstone/event/ClientEventHandler","handleTooltip","tombstone-neoforge-1.21.1-9.5.5.jar","8"})
    );

    private static final ConcurrentLinkedQueue<String> APPLIED = new ConcurrentLinkedQueue<>();

    private static final Map<String, String> OPTIONAL_TARGETS = Map.of(
            "SimplyTooltipsRendererMixin", "net.sweenus.simplytooltips.client.render.TooltipRenderer",
            "JeiTooltipMixin", "mezz.jei.common.gui.JeiTooltip",
            "JeiIngredientGridMixin", "mezz.jei.gui.overlay.ingredients.IngredientGrid",
            "JeiRenderHelperMixin", "mezz.jei.neoforge.platform.RenderHelper",
            "BetterAdvancedTooltipsMixin", "dev.latvian.mods.betteradvancedtooltips.BATClientEventHandler",
            "ImmersiveUiRenderMixin", "it.hurts.octostudios.immersiveui.util.CommonCode",
            "IconLeadingTooltipMixin", "net.pixeldreamstudios.iconleadingtooltip.neoforge.client.IconLeadingTooltipNeoForgeClient"
    );

    private static final Map<String, String> OPTIONAL_OWNERS = Map.of(
            "SimplyTooltipsRendererMixin", "SIMPLYTOOLTIPS",
            "JeiTooltipMixin", "JEI",
            "JeiIngredientGridMixin", "JEI",
            "JeiRenderHelperMixin", "JEI",
            "BetterAdvancedTooltipsMixin", "BETTER_ADVANCED_TOOLTIPS",
            "ImmersiveUiRenderMixin", "IMMERSIVEUI",
            "IconLeadingTooltipMixin", "ICON_LEADING_TOOLTIP"
    );

    private static final Map<String, String> OPTIONAL_MOD_IDS = Map.of(
            "SimplyTooltipsRendererMixin", "simplytooltips",
            "JeiTooltipMixin", "jei",
            "JeiIngredientGridMixin", "jei",
            "JeiRenderHelperMixin", "jei",
            "BetterAdvancedTooltipsMixin", "betteradvancedtooltips",
            "ImmersiveUiRenderMixin", "immersiveui",
            "IconLeadingTooltipMixin", "icon_leading_tooltip"
    );

    private static final ConcurrentHashMap<String, ResourceCheck> BOOTSTRAP_CHECKS = new ConcurrentHashMap<>();

    public enum ResourceStatus {
        PRESENT,
        ABSENT,
        CHECK_FAILED
    }

    public record ResourceCheck(ResourceStatus status, String loader, Throwable error) {}

    public static List<String> appliedSnapshot() {
        return List.copyOf(APPLIED);
    }

    public static Map<String, String> optionalTargetsSnapshot() {
        return Map.copyOf(OPTIONAL_TARGETS);
    }

    public static Map<String, String> optionalOwnersSnapshot() {
        return Map.copyOf(OPTIONAL_OWNERS);
    }

    public static Map<String, String> optionalModIdsSnapshot() {
        return Map.copyOf(OPTIONAL_MOD_IDS);
    }

    public static ResourceCheck bootstrapCheck(String mixinSimpleName) {
        return BOOTSTRAP_CHECKS.get(mixinSimpleName);
    }

    @Override
    public void onLoad(String mixinPackage) {
        bootLog("MIXIN-PLUGIN-BOOTSTRAP-SAFE version=2.2.2 strategy=class-resource-only no-fml-runtime-state=true");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String simpleName = mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1);
        String[] candidate = UPSTREAM_CANDIDATES.get(simpleName);
        if (candidate != null) return verifiedCandidate(candidate);
        String target = OPTIONAL_TARGETS.get(simpleName);
        if (target == null) {
            return true;
        }

        String owner = OPTIONAL_OWNERS.get(simpleName);
        String modId = OPTIONAL_MOD_IDS.get(simpleName);
        String resourceName = target.replace('.', '/') + ".class";
        ResourceCheck check = checkClassResource(resourceName);
        BOOTSTRAP_CHECKS.put(simpleName, check);

        if (check.status() == ResourceStatus.PRESENT) {
            bootLog("OPTIONAL-MIXIN ENABLED owner=" + owner
                    + " modId=" + modId
                    + " version=<deferred-bootstrap> modPresence=DEFERRED_BOOTSTRAP mixin=" + simpleName
                    + " target=" + target
                    + " detection=target-class-resource loader=" + check.loader()
                    + " reason=target-class-resource-present");
            return true;
        }

        if (check.status() == ResourceStatus.ABSENT) {
            bootLog("OPTIONAL-MIXIN SKIPPED owner=" + owner
                    + " modId=" + modId
                    + " version=<deferred-bootstrap> modPresence=DEFERRED_BOOTSTRAP mixin=" + simpleName
                    + " target=" + target
                    + " detection=target-class-resource reason=target-class-resource-absent-bootstrap");
            return false;
        }

        bootLog("OPTIONAL-DETECTION-DEFERRED owner=" + owner
                + " modId=" + modId
                + " version=<deferred-bootstrap> modPresence=UNKNOWN mixin=" + simpleName
                + " target=" + target
                + " detection=target-class-resource reason=resource-check-failed error=" + errorType(check.error())
                + " action=SAFE-SKIP");
        bootLog("OPTIONAL-MIXIN SKIPPED owner=" + owner
                + " modId=" + modId
                + " mixin=" + simpleName
                + " target=" + target
                + " reason=detection-unavailable-at-bootstrap action=SAFE-SKIP");
        return false;
    }

    private static ResourceCheck checkClassResource(String resourceName) {
        try {
            ClassLoader pluginLoader = DiagnosticMixinPlugin.class.getClassLoader();
            if (pluginLoader != null) {
                URL resource = pluginLoader.getResource(resourceName);
                if (resource != null) {
                    return new ResourceCheck(ResourceStatus.PRESENT, "plugin-classloader", null);
                }
            }

            ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
            if (contextLoader != null && contextLoader != pluginLoader) {
                URL resource = contextLoader.getResource(resourceName);
                if (resource != null) {
                    return new ResourceCheck(ResourceStatus.PRESENT, "context-classloader", null);
                }
            }

            return new ResourceCheck(ResourceStatus.ABSENT, "searched-plugin-and-context-classloaders", null);
        } catch (Throwable t) {
            return new ResourceCheck(ResourceStatus.CHECK_FAILED, "resource-lookup-failed", t);
        }
    }

    private static String errorType(Throwable t) {
        return t == null ? "<none>" : t.getClass().getName();
    }

    private static void bootLog(String message) {
        System.out.println("[CT-BN-DIAG] " + message);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        String entry = mixinClassName + " -> " + targetClassName;
        APPLIED.add(entry);
        bootLog("MIXIN-APPLIED " + entry);
    }
    private static boolean verifiedCandidate(String[] c) {
        String resource = c[0]+".class";
        for (ClassLoader loader : new ClassLoader[]{DiagnosticMixinPlugin.class.getClassLoader(),Thread.currentThread().getContextClassLoader()}) {
            if(loader==null)continue;
            try (java.io.InputStream input=loader.getResourceAsStream(resource)) {
                if(input==null)continue;
                ClassNode node=new ClassNode();
                new org.objectweb.asm.ClassReader(input).accept(node,org.objectweb.asm.ClassReader.SKIP_CODE|org.objectweb.asm.ClassReader.SKIP_DEBUG);
                boolean found=node.methods.stream().anyMatch(method->method.name.equals(c[1])
                        &&method.desc.equals("(Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;)V")
                        &&(method.access&8)==Integer.parseInt(c[3]));
                bootLog("UPSTREAM-CANDIDATE "+(found?"ENABLED":"SKIPPED")+" class="+c[0]+" method="+c[1]+" auditedJar="+c[2]+" runtimeOrder=UNPROVEN");
                return found;
            } catch (Exception failure) {
                bootLog("UPSTREAM-CANDIDATE SKIPPED class="+c[0]+" reason=resource-check-failed");return false;
            }
        }
        return false;
    }
}
