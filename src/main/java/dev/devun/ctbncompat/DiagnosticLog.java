package dev.devun.ctbncompat;

import dev.devun.ctbncompat.DiagnosticMixinPlugin.ResourceCheck;
import dev.devun.ctbncompat.DiagnosticMixinPlugin.ResourceStatus;
import dev.devun.ctbncompat.bridge.AbstractContainerScreenBridge;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DiagnosticLog {
    private static final Logger LOGGER = LoggerFactory.getLogger("CT-BN-DIAG");
    private static final AtomicLong SEQ = new AtomicLong();
    private static final Set<String> ONCE = ConcurrentHashMap.newKeySet();

    private static volatile long frame;
    private static volatile ItemStack hoveredStack;
    private static volatile String colorTooltipsLogicalState = "none";
    private static volatile String colorTooltipsState = "state-not-yet-captured";
    private static volatile String lastCtAction = "";
    private static volatile String lastCtNoLiveItemState = "";
    private static volatile long lastCtNoLiveItemNs;
    private static final long CT_NO_LIVE_ITEM_HEARTBEAT_NS = 5_000_000_000L;

    private static volatile long lastJeiHoverNs;
    private static volatile String lastJeiItemId = "none";
    private static volatile ItemStack lastJeiStack;

    private static final Map<String, String> EXPECTED_OPTIONAL_VERSIONS = Map.of(
            "simplytooltips", "0.1.5",
            "jei", "19.44.0.403",
            "betteradvancedtooltips", "2101.1.0-build.5",
            "immersiveui", "0.3.3",
            "icon_leading_tooltip", "1.0.8"
    );

    private static volatile boolean runtimeVerificationComplete;
    private static int runtimeVerificationAttempts;
    private static final int MAX_RUNTIME_VERIFICATION_ATTEMPTS = 200;

    private DiagnosticLog() {}

    public static void log(String message) {
        LOGGER.info("[CT-BN-DIAG #" + SEQ.incrementAndGet() + "] " + message);
    }

    public static void applied(List<String> applied) {
        for (String entry : applied) {
            log("MIXIN-APPLIED-SNAPSHOT " + entry);
        }
    }

    public static void verifiedPrimaryHooks() {
        log("PRIMARY-HOOK-VERIFIED mod=SIMPLYTOOLTIPS target=net.sweenus.simplytooltips.client.render.TooltipRenderer method=render(GuiGraphics,Font,ItemStack,List,TooltipProvider,int,int,int,int) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=SIMPLYTOOLTIPS target=net.sweenus.simplytooltips.client.render.TooltipRenderer method=render(GuiGraphics,Font,ItemStack,List,TooltipProvider,List,int,int,int,int) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=JEI target=mezz.jei.gui.overlay.ingredients.IngredientGrid method=drawTooltips(Minecraft,GuiGraphics,int,int) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=JEI target=mezz.jei.gui.overlay.ingredients.IngredientGrid method=drawTooltip(GuiGraphics,int,int,IElement) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=JEI target=mezz.jei.common.gui.JeiTooltip method=prepareForIngredientTooltip(ITypedIngredient,IIngredientRenderer,IIngredientManager) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=JEI target=mezz.jei.common.gui.JeiTooltip method=draw(GuiGraphics,int,int,ITypedIngredient,IIngredientRenderer,IIngredientManager) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=JEI target=mezz.jei.neoforge.platform.RenderHelper method=renderTooltip(GuiGraphics,List,int,int,Font,ItemStack) require=1");
        log("PRIMARY-HOOK-VERIFIED mod=BETTER_ADVANCED_TOOLTIPS target=dev.latvian.mods.betteradvancedtooltips.BATClientEventHandler method=onItemTooltip(ItemTooltipEvent) require=1 static=true");
        log("PRIMARY-HOOK-VERIFIED mod=IMMERSIVEUI target=it.hurts.octostudios.immersiveui.util.CommonCode method=renderFloating(...) require=1 static=true");
        log("PRIMARY-HOOK-VERIFIED mod=ICON_LEADING_TOOLTIP target=net.pixeldreamstudios.iconleadingtooltip.neoforge.client.IconLeadingTooltipNeoForgeClient method=onItemTooltip(ItemTooltipEvent) require=1 static=true");
    }

    public static void hookExecuted(String mod, String hook) {
        once("hook-executed:" + mod + ":" + hook, "PRIMARY-HOOK-EXECUTED mod=" + mod + " hook=" + hook);
    }

    public static void once(String key, String message) {
        if (ONCE.add(key)) {
            log(message);
        }
    }

    public static void beginFrame(Object screen, int mouseX, int mouseY) {
        frame++;
        TooltipTrace.beginFrame(frame, screen);
        once("screen", "SCREEN-RENDER hook ACTIVE");
    }

    public static void screenHoveredProbe(Object screen, int mouseX, int mouseY) {
        if (!(screen instanceof AbstractContainerScreen<?> container)
                || !(container instanceof AbstractContainerScreenBridge bridge)) {
            return;
        }
        logSlot("HOVERED-SLOT source=TYPED-ACCESSOR", screen, bridge.ctbn$getHoveredSlot(), mouseX, mouseY, false);
    }

    public static void liveContainerFrame(Object screen, Slot hoveredSlot, int mouseX, int mouseY) {
        once("live-screen-frame-first", "LIVE-SCREEN-FRAME FIRST target=AbstractContainerScreen.render@RETURN");
        logSlot("LIVE-HOVER", screen, hoveredSlot, mouseX, mouseY, true);
    }

    public static void liveTooltipCall(String phase, Object screen, Slot hoveredSlot, int mouseX, int mouseY) {
        ItemStack stack = stack(hoveredSlot);
        if (stack != null && !TooltipTrace.recent()) hoveredStack = stack;
        TooltipTrace.normal("live-tooltip:"+phase, TooltipTrace.signature(stack),
                "LIVE-TOOLTIP-CALL phase="+phase+" item="+TooltipTrace.signature(stack));
    }

    private static void logSlot(String prefix, Object screen, Slot slot, int mouseX, int mouseY, boolean live) {
        ItemStack stack = stack(slot);
        if (stack == null) return;
        if (!TooltipTrace.recent()) hoveredStack = stack;
        TooltipTrace.normal(prefix, TooltipTrace.signature(stack), prefix+" item="+TooltipTrace.signature(stack));
    }

    public static void reportTooltipMods() {
        ModList modList = ModList.get();
        if (modList == null) {
            log("OPTIONAL-RUNTIME-VERIFICATION INVALID reason=modlist-not-initialized runtimeVerification=false");
            return;
        }

        Map<String, IModInfo> runtimeMods = new HashMap<>();
        for (IModInfo info : modList.getMods()) {
            runtimeMods.put(info.getModId(), info);
        }

        Map<String, String> targets = DiagnosticMixinPlugin.optionalTargetsSnapshot();
        Map<String, String> owners = DiagnosticMixinPlugin.optionalOwnersSnapshot();
        Map<String, String> modIds = DiagnosticMixinPlugin.optionalModIdsSnapshot();

        for (String mixin : targets.keySet()) {
            String owner = owners.get(mixin);
            String modId = modIds.get(mixin);
            String target = targets.get(mixin);
            ResourceCheck bootstrap = DiagnosticMixinPlugin.bootstrapCheck(mixin);
            IModInfo info = runtimeMods.get(modId);
            boolean runtimePresent = info != null;
            String actualVersion = runtimePresent ? String.valueOf(info.getVersion()) : "<absent>";
            String expectedVersion = EXPECTED_OPTIONAL_VERSIONS.get(modId);

            if (bootstrap == null || bootstrap.status() == ResourceStatus.CHECK_FAILED) {
                log("OPTIONAL-RUNTIME-VERIFICATION owner=" + owner
                        + " modId=" + modId
                        + " runtimePresent=" + runtimePresent
                        + " actualVersion=" + actualVersion
                        + " bootstrapClass=CHECK_FAILED final=INVALID reason=bootstrap-class-check-failed");
                continue;
            }

            boolean bootstrapPresent = bootstrap.status() == ResourceStatus.PRESENT;
            String finalStatus;
            if (bootstrapPresent && runtimePresent) {
                finalStatus = "VERIFIED";
            } else if (!bootstrapPresent && !runtimePresent) {
                finalStatus = "CONSISTENT-ABSENT";
            } else {
                finalStatus = "INVALID";
            }

            String reason;
            if (bootstrapPresent && !runtimePresent) {
                reason = " reason=bootstrap-class-present-runtime-mod-absent";
            } else if (!bootstrapPresent && runtimePresent) {
                reason = " reason=runtime-mod-present-bootstrap-target-class-absent";
            } else {
                reason = "";
            }

            log("OPTIONAL-RUNTIME-VERIFICATION owner=" + owner
                    + " modId=" + modId
                    + " expectedVersion=" + expectedVersion
                    + " actualVersion=" + actualVersion
                    + " runtimePresent=" + runtimePresent
                    + " bootstrapClass=" + bootstrap.status()
                    + " target=" + target
                    + " final=" + finalStatus + reason);

            if (!reason.isEmpty()) {
                log("DIAGNOSTIC-INVARIANT-FAILED owner=" + owner
                        + " modId=" + modId + reason
                        + " target=" + target);
            }

            if (runtimePresent && expectedVersion != null && !expectedVersion.equals(actualVersion)) {
                log("DIAGNOSTIC-INVARIANT-FAILED owner=" + owner
                        + " modId=" + modId
                        + " reason=dependency-version-mismatch expected=" + expectedVersion
                        + " actual=" + actualVersion
                        + " diagnosticEvidence=INVALID");
            }
        }
    }

    public static void hoveredSlot(Object screen, int mouseX, int mouseY) {
        once("hover", "HOVERED-SLOT hook ACTIVE");
        if (screen instanceof AbstractContainerScreen<?> container
                && container instanceof AbstractContainerScreenBridge bridge) {
            logSlot("HOVERED-SLOT", screen, bridge.ctbn$getHoveredSlot(), mouseX, mouseY, false);
        }
    }

    public static void tooltipBegin(String path, ItemStack directStack, Object tooltip, int x, int y) {
        once("gui", "GUI-TOOLTIP hook ACTIVE");
        ItemStack stack = directStack != null ? directStack : (TooltipTrace.recent() ? lastJeiStack : hoveredStack);
        TooltipTrace.point("GuiGraphics."+path, stack, tooltip);
    }

    public static void tooltipInternal(Object tooltip, int x, int y, Object positioner) {
        ItemStack stack = TooltipTrace.recent() ? lastJeiStack : hoveredStack;
        TooltipTrace.point("GuiGraphics.renderTooltipInternal", stack, tooltip);
    }

    public static void ctObserved(String source, ItemStack stack) {
        TooltipTrace.point("ColorTooltips."+source, stack, null, "capturedState="+colorTooltipsState);
        TooltipTrace.normal("ct-active:"+source, TooltipTrace.signature(stack),
                "COLORTOOLTIPS source="+source+" item="+TooltipTrace.signature(stack)
                +" ITEM-IDENTITY="+hex(stack==null?0:System.identityHashCode(stack))
                +" capturedState="+colorTooltipsState);
    }

    public static void captureColorTooltipsState(
            ItemStack activeStack,
            boolean visibleRequested,
            boolean isTextOnlyMode,
            boolean needsAnimatorReset,
            long lostCandidateStartTimeMs,
            long switchFlashStartTimeMs,
            long fadeInStartTimeMs,
            Object lastState
    ) {
        colorTooltipsLogicalState = TooltipTrace.signature(activeStack)+"|"+visibleRequested+"|"+isTextOnlyMode+"|"+needsAnimatorReset;
        colorTooltipsState = "activeStack@" + hex(activeStack == null ? 0 : System.identityHashCode(activeStack))
                + " visibleRequested=" + visibleRequested
                + " isTextOnlyMode=" + isTextOnlyMode
                + " needsAnimatorReset=" + needsAnimatorReset
                + " lostCandidateStartTimeMs=" + lostCandidateStartTimeMs
                + " switchFlashStartTimeMs=" + switchFlashStartTimeMs
                + " fadeInStartTimeMs=" + fadeInStartTimeMs
                + " lastState=" + String.valueOf(lastState);
    }

    public static void ctState(String action) {
        if ("onFrameWithoutLiveItem()".equals(action)) { ctNoLiveItemState(action); return; }
        TooltipTrace.normal("ct-state:"+action, colorTooltipsLogicalState,
                "COLORTOOLTIPS action="+action+" "+colorTooltipsState);
        lastCtAction=action;
    }

    private static void ctNoLiveItemState(String action) {
        long now = System.nanoTime();
        String state = action + "|" + colorTooltipsLogicalState;
        boolean entering = !action.equals(lastCtAction);
        boolean changed = !state.equals(lastCtNoLiveItemState);
        boolean heartbeat = lastCtNoLiveItemNs == 0L || now - lastCtNoLiveItemNs >= CT_NO_LIVE_ITEM_HEARTBEAT_NS;
        if (entering || changed || heartbeat) {
            String reason = entering ? "enter-state" : changed ? "state-change" : "heartbeat";
            log("COLORTOOLTIPS " + action
                    + " frame=" + frame
                    + " dedupe=" + reason
                    + " heartbeatMaxSeconds=5"
                    + " " + colorTooltipsState);
            lastCtNoLiveItemState = state;
            lastCtNoLiveItemNs = now;
        }
        lastCtAction = action;
    }

    public static void lifecycle(Object lifecycleType) {
        TooltipTrace.normal("lifecycle", String.valueOf(lifecycleType), "COLORTOOLTIPS LIFECYCLE="+lifecycleType);
    }

    public static void animator(String action) {
        TooltipTrace.normal("animator:"+action, colorTooltipsLogicalState,
                "COLORTOOLTIPS ANIMATOR="+action+" "+colorTooltipsState);
    }

    public static void jeiGridDrawTooltips(int mouseX, int mouseY, String runtimeMethod) {
        hookExecuted("JEI", runtimeMethod);
        TooltipTrace.normal("jei-grid", "active", "JEI-TOOLTIP-PATH path="+runtimeMethod);
    }

    public static void jeiIngredientHover(
            Object element,
            Object typedIngredient,
            Object ingredient,
            Object ingredientType,
            ItemStack stack,
            int mouseX,
            int mouseY,
            String runtimeMethod
    ) {
        hookExecuted("JEI", runtimeMethod);
        rememberJeiStack(stack);
        lastJeiHoverNs = System.nanoTime();
        TooltipTrace.hover(stack, mouseX, mouseY);
    }

    public static void jeiTooltipPrepare(
            ItemStack stack,
            Object typedIngredient,
            Object ingredient,
            Object ingredientType,
            Object renderer,
            Object ingredientManager,
            String runtimeMethod
    ) {
        hookExecuted("JEI", runtimeMethod);
        rememberJeiStack(stack);
        // The Mixin passes real getLines snapshots at both HEAD and RETURN. No inferred contents here.
    }

    public static void jeiTooltipDraw(
            Object tooltip,
            List<?> lines,
            ItemStack stack,
            int mouseX,
            int mouseY,
            Object typedIngredient,
            Object ingredient,
            Object ingredientType,
            Object renderer,
            Object ingredientManager,
            String runtimeMethod
    ) {
        hookExecuted("JEI", runtimeMethod);
        rememberJeiStack(stack);
        TooltipTrace.point("JeiTooltip.draw", stack, lines);
    }

    public static void jeiRenderHandoff(ItemStack stack, List<?> tooltipElements, int mouseX, int mouseY, String runtimeMethod) {
        hookExecuted("JEI", runtimeMethod);
        rememberJeiStack(stack);
        TooltipTrace.point("RenderHelper.renderTooltip", stack, tooltipElements);
    }

    public static void owner(String mod) {
        ItemStack stack = TooltipTrace.recent() ? lastJeiStack : hoveredStack;
        TooltipTrace.normal("owner:"+mod+":"+TooltipTrace.recent(), TooltipTrace.signature(stack),
                "TOOLTIP-OWNER mod="+mod+" item="+TooltipTrace.signature(stack));
    }

    private static void logJeiPath(String message, String state, boolean force) {
        TooltipTrace.normal("path:"+state.split("\\|")[0], state, message);
    }

    private static void logJeiFollowup(String owner, String path, ItemStack stack, int mouseX, int mouseY) {
        if (TooltipTrace.recent()) TooltipTrace.point(owner+"."+path, stack, null);
    }

    private static boolean recentJeiHover() {
        return TooltipTrace.recent();
    }

    private static void rememberJeiStack(ItemStack stack) {
        if (stack == null) {
            return;
        }
        hoveredStack = stack;
        lastJeiStack = stack;
        lastJeiItemId = itemId(stack);
    }

    private static String callerMods() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        LinkedHashSet<String> mods = new LinkedHashSet<>();
        for (StackTraceElement element : trace) {
            String name = element.getClassName().toLowerCase(Locale.ROOT);
            if (name.contains("jei")) mods.add("JEI");
            if (name.contains("quark")) mods.add("QUARK");
            if (name.contains("simplytooltip")) mods.add("SIMPLYTOOLTIPS");
            if (name.contains("betteradvanced") || name.contains("advancedtooltip")) mods.add("BETTER-ADVANCED-TOOLTIPS");
            if (name.contains("icon") && name.contains("tooltip")) mods.add("ICON-LEADING-TOOLTIP");
            if (name.contains("rarity")) mods.add("RARITYCORE/INTEGRATION");
            if (name.contains("immersiveui")) mods.add("IMMERSIVEUI");
            if (name.contains("colortooltips")) mods.add("COLORTOOLTIPS");
            if (name.contains("betternether")) mods.add("BETTERNETHER");
        }
        return mods.isEmpty() ? "none" : String.join(",", mods);
    }

    private static ItemStack stack(Slot slot) {
        if (slot == null) {
            return null;
        }
        ItemStack stack = slot.getItem();
        return stack == null || stack.isEmpty() ? null : stack;
    }

    private static boolean isBetterNetherId(String id) {
        String lower = id.toLowerCase(Locale.ROOT);
        return lower.contains("betternether") || lower.contains("better_nether");
    }

    public static String itemId(ItemStack stack) {
        if (stack == null) {
            return "none";
        }
        DefaultedRegistry<Item> itemRegistry = BuiltInRegistries.ITEM;
        return String.valueOf(itemRegistry.getKey(stack.getItem()));
    }

    public static int componentHash(ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        DataComponentMap components = stack.getComponents();
        return components == null ? 0 : components.hashCode();
    }

    private static int listSize(Object value) {
        return value instanceof List<?> list ? list.size() : -1;
    }

    private static String className(Object value) {
        return value == null ? "null" : value.getClass().getName();
    }

    private static String hex(int value) {
        return Integer.toHexString(value);
    }

    public static synchronized void reportTooltipModsDeferred() {
        if (runtimeVerificationComplete) {
            return;
        }
        if (runtimeVerificationAttempts >= MAX_RUNTIME_VERIFICATION_ATTEMPTS) {
            return;
        }

        runtimeVerificationAttempts++;
        ModList modList = ModList.get();
        if (modList == null) {
            if (runtimeVerificationAttempts == 1) {
                log("OPTIONAL-RUNTIME-VERIFICATION DEFERRED reason=modlist-not-initialized retry=true");
            }
            if (runtimeVerificationAttempts >= MAX_RUNTIME_VERIFICATION_ATTEMPTS) {
                log("OPTIONAL-RUNTIME-VERIFICATION ABANDONED reason=modlist-not-initialized retry-cap=200 startup-failure=false");
            }
            return;
        }

        reportTooltipMods();
        runtimeVerificationComplete = true;
        log("OPTIONAL-RUNTIME-VERIFICATION COMPLETE lifecycle=ClientTickEvent.Post retry=false");
    }
}
