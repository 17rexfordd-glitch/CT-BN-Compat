package dev.devun.ctbncompat.bridge;

import net.minecraft.world.inventory.Slot;

/** Runtime-facing bridge kept outside the defined Mixin package. */
public interface AbstractContainerScreenBridge {
    Slot ctbn$getHoveredSlot();
}
