# CT-BN-Compat v2.3.2

Purpose: fix only the compatibility stabilizer `IncompatibleClassChangeError` from the restored-v2.2.8-based v2.3.1 build. The working v2.2.8 detector/tracer/observer system remains intact.

## Cause

`BetterNetherCurseNameStabilizer.class` was compiled against a bad compatibility stub where `net.minecraft.network.chat.MutableComponent` was treated as an interface. Actual Minecraft/ImmersiveUI 1.21.1 bytecode treats `MutableComponent` as a class for these method calls, so the stabilizer emitted `InterfaceMethodref` / `invokeinterface` entries for `getStyle`, `setStyle`, and `getSiblings`. At runtime that class/interface mismatch caused `java.lang.IncompatibleClassChangeError`, so the stabilizer fell back to the original tooltip.

## Correction

- Kept every restored v2.2.8 detector, tracer, observer, diagnostic hook, and logging path.
- Kept the v2.3.1 compatibility path and target enchantment scope.
- Corrected final packaged `BetterNetherCurseNameStabilizer.class` so `MutableComponent.getStyle`, `MutableComponent.setStyle`, and `MutableComponent.getSiblings` are class Methodrefs with `invokevirtual`, not InterfaceMethodrefs/`invokeinterface`.
- Stabilization remains enchantment-based for only:
  - `betternether:ruby_fire`
  - `betternether:obsidian_breaker`
- Tooltip descriptions, levels, JEI behavior, BetterNether functionality, unrelated ImmersiveUI behavior, and unrelated ColorTooltips behavior are preserved.

## Verification

- Version markers: `2.3.2`.
- `behavioral-fixes=true` remains present.
- No detector was removed, disabled, simplified, or replaced.
- v2.2.8 detector classes and mixins remain packaged, including `EnchantmentTrace`, `TooltipTrace`, JEI hooks, ItemStack hooks, Enchantment section/name hooks, and `ImmersiveEnchantmentStyleMixin`.
- `BetterNetherCurseNameStabilizer` remains packaged.
- `BetterNetherCurseNameStabilizer.class` has no `invokeinterface` calls targeting `net.minecraft.network.chat.MutableComponent`.
- `BetterNetherCurseNameStabilizer.class` has `invokevirtual` calls for `MutableComponent.getStyle`, `MutableComponent.setStyle`, and `MutableComponent.getSiblings`.
- No stale broken diagnostic API calls remain: no `Component.visit(BiFunction, Style)`, no `EnchantmentTags.CURSE:Object`.
- No tooltip rendering cancellation, tooltip-list replacement, animator reset redirect, or same-item redirect is packaged.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, MixinExtras, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.2.jar`
- SHA-256: `31f0e09f8f708f07b7b7112d28cd0a309e9825e5b95ee3d02508922824eed199`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.3.2-source.zip`