package dev.devun.ctbncompat;

import com.mojang.blaze3d.platform.Window;
import dev.devun.ctbncompat.bridge.AbstractContainerScreenBridge;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public final class ClientRuntimeDiagnostics {
    private static long ticks;
    private static String lastScreenClass = "<unset>";
    private static String lastHoverState = "";
    private static long lastHoverLogNs;
    private static final long HOVER_HEARTBEAT_NS = 5_000_000_000L;
    private static Screen lastHoverScreen;
    private static boolean reportedAnyScreen;

    private ClientRuntimeDiagnostics() {}

    public static void onClientTick(ClientTickEvent.Post event) {
        ticks++;
        DiagnosticLog.once(
                "client-runtime-anchor",
                "CLIENT-RUNTIME-ANCHOR FIRST event=ClientTickEvent.Post access=typed"
        );
        RuntimeMetadataGuard.reportTooltipModsDeferred();

        if (ticks <= 3) {
            DiagnosticLog.log("CLIENT-RUNTIME-ANCHOR tick=" + ticks + " access=typed");
        }

        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        if (screen != lastHoverScreen) {
            lastHoverState = "";
            lastHoverScreen = screen;
        }
        if (screen == null) {
            if (!"null".equals(lastScreenClass)) {
                DiagnosticLog.log("ACTIVE-SCREEN screen=null tick=" + ticks);
            }
            lastScreenClass = "null";
            return;
        }

        String screenClass = screen.getClass().getName();
        String superclass = screen.getClass().getSuperclass() == null
                ? "null"
                : screen.getClass().getSuperclass().getName();
        MousePos mouse = mouse(minecraft);
        boolean isContainer = screen instanceof AbstractContainerScreen<?>;
        String menuClass = "none";
        if (isContainer) {
            AbstractContainerMenu menu = ((AbstractContainerScreen<?>) screen).getMenu();
            if (menu != null) {
                menuClass = menu.getClass().getName();
            }
        }

        if (!screenClass.equals(lastScreenClass) || !reportedAnyScreen) {
            DiagnosticLog.log("ACTIVE-SCREEN screen=" + screenClass
                    + " superclass=" + superclass
                    + " instanceofScreen=true instanceofAbstractContainerScreen=" + isContainer
                    + " mouse=" + mouse.x + "," + mouse.y
                    + " tick=" + ticks
                    + " menu=" + menuClass
                    + " access=typed");
            reportedAnyScreen = true;
            lastScreenClass = screenClass;
        }

        if (screen instanceof AbstractContainerScreen<?> container) {
            inspectContainer(container, mouse);
        }
    }

    private static void inspectContainer(AbstractContainerScreen<?> screen, MousePos mouse) {
        AbstractContainerMenu menu = screen.getMenu();
        if (menu == null) {
            lastHoverState = "";
            DiagnosticLog.once(
                    "container-menu-null:" + screen.getClass().getName(),
                    "CONTAINER-INSPECTION-FAILED screen=" + screen.getClass().getName()
                            + " reason=menu-null access=typed"
            );
            return;
        }

        NonNullList<Slot> slots = menu.slots;
        DiagnosticLog.once(
                "active-container:" + screen.getClass().getName() + ":" + menu.getClass().getName(),
                "ACTIVE-CONTAINER screen=" + screen.getClass().getName()
                        + " menu=" + menu.getClass().getName()
                        + " slotCount=" + slots.size()
                        + " mouse=" + mouse.x + "," + mouse.y
                        + " tick=" + ticks
                        + " hoverSource=authoritative-accessor"
        );

        if (!(screen instanceof AbstractContainerScreenBridge bridge)) {
            lastHoverState = "";
            DiagnosticLog.once(
                    "container-accessor-missing:" + screen.getClass().getName(),
                    "CONTAINER-INSPECTION-FAILED screen=" + screen.getClass().getName()
                            + " reason=hoveredSlot-accessor-not-applied no-fallback=true"
            );
            return;
        }

        Slot hoveredSlot = bridge.ctbn$getHoveredSlot();
        if (hoveredSlot == null) {
            lastHoverState = "";
            return;
        }

        ItemStack stack = hoveredSlot.getItem();
        if (stack == null || stack.isEmpty()) {
            lastHoverState = "";
            return;
        }

        int slotIndex = slots.indexOf(hoveredSlot);
        DefaultedRegistry<Item> itemRegistry = BuiltInRegistries.ITEM;
        String itemId = String.valueOf(itemRegistry.getKey(stack.getItem()));
        int identity = System.identityHashCode(stack);
        DataComponentMap components = stack.getComponents();
        int componentHash = components == null ? 0 : components.hashCode();
        int count = stack.getCount();
        String hoverState = screen.getClass().getName() + "|" + slotIndex + "|" + itemId + "|"
                + componentHash + "|" + count;

        String lower = itemId.toLowerCase(Locale.ROOT);
        boolean betterNether = lower.startsWith("betternether:") || lower.startsWith("better_nether:");

        long now = System.nanoTime();
        if (!hoverState.equals(lastHoverState)
                || (betterNether && now - lastHoverLogNs >= HOVER_HEARTBEAT_NS)) {
            DiagnosticLog.log("RUNTIME-HOVER screen=" + screen.getClass().getName()
                    + " menu=" + menu.getClass().getName()
                    + " slot=" + slotIndex
                    + " item=" + itemId
                    + " count=" + count
                    + " ITEM-IDENTITY=" + Integer.toHexString(identity)
                    + " componentHash=" + componentHash
                    + " mouse=" + mouse.x + "," + mouse.y
                    + " tick=" + ticks
                    + " source=authoritative-hoveredSlot-accessor");

            if (betterNether) {
                DiagnosticLog.log("RUNTIME-HOVER BETTERNETHER ITEM item=" + itemId
                        + " slot=" + slotIndex
                        + " ITEM-IDENTITY=" + Integer.toHexString(identity)
                        + " componentHash=" + componentHash
                        + " tick=" + ticks);
            }
            lastHoverState = hoverState;
            lastHoverLogNs = now;
        }
    }

    private static MousePos mouse(Minecraft minecraft) {
        MouseHandler mouse = minecraft.mouseHandler;
        Window window = minecraft.getWindow();
        if (mouse == null || window == null) {
            return new MousePos(-1, -1);
        }

        int screenWidth = Math.max(1, window.getScreenWidth());
        int screenHeight = Math.max(1, window.getScreenHeight());
        int x = (int) (mouse.xpos() * window.getGuiScaledWidth() / (double) screenWidth);
        int y = (int) (mouse.ypos() * window.getGuiScaledHeight() / (double) screenHeight);
        return new MousePos(x, y);
    }

    private record MousePos(int x, int y) {}
}
