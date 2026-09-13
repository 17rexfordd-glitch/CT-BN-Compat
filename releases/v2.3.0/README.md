# CT-BN-Compat v2.3.0

Purpose: reset to the v2.2.8 code base because that was the last build where the BetterNether detector/tracing worked, then add the narrow compatibility fix on top.

## Base reset

- Restored the v2.2.8 implementation as the source/runtime base.
- Preserved the working v2.2.8 detector/tracing for `betternether:ruby_fire`, `betternether:obsidian_breaker`, `dark_red`, `obfuscated=true`, frame-to-frame component changes, and `Enchantment.getFullname()` before/after state.
- Did not use the stripped-down v2.3.1/v2.3.2 detector line as the base.

## Compatibility fix

- Added `BetterNetherCurseNameStabilizer`.
- The existing `EnchantmentNameMixin` RETURN observer now calls `EnchantmentTrace.compatibilityReturn(...)` before completing the normal v2.2.8 `nameReturn(...)` detector flow.
- Only these enchantments are stabilized:
  - `betternether:ruby_fire`
  - `betternether:obsidian_breaker`
- The returned enchantment-name component is copied and recursively normalized to red + `obfuscated=false`.
- Enchantment levels, description lines, tooltip list structure, item data, BetterNether behavior, unrelated ImmersiveUI formatting, and unrelated ColorTooltips behavior are left alone.

## Detector proof kept

- Pre-fix component state is sampled as `Enchantment.getFullname.COMPAT-BEFORE`.
- Post-fix component state is sampled as `Enchantment.getFullname.COMPAT-AFTER`.
- `CT-BN-DETECT` records `beforeObfuscated`, `afterObfuscated`, `postFixSHA256`, and `changedFromPreviousFrame` with strong deduplication.
- The old v2.2.8 JEI/ItemStack/Enchantment/RenderUtils evidence path remains packaged.

## Verification

- Version markers: `2.3.0`.
- `behavioral-fixes=true` is present.
- v2.2.8 detector classes and mixins remain packaged, including `EnchantmentTrace`, `TooltipTrace`, JEI hooks, ItemStack hooks, Enchantment section/name hooks, and `ImmersiveEnchantmentStyleMixin`.
- `BetterNetherCurseNameStabilizer` is packaged.
- `EnchantmentNameMixin` calls `EnchantmentTrace.compatibilityReturn(...)`, conditionally calls `CallbackInfoReturnable.setReturnValue(...)`, then continues `EnchantmentTrace.nameReturn(...)`.
- `@Inject` annotations are runtime-visible and `@Mixin` remains runtime-invisible.
- NeoForge `@Mod` is runtime-visible.
- No stale broken diagnostic API calls remain: no `Component.visit(BiFunction, Style)`, no `EnchantmentTags.CURSE:Object`.
- No tooltip rendering cancellation, tooltip-list replacement, animator reset redirect, or same-item redirect is packaged.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.0.jar`
- SHA-256: `48fd38cd843554b2fbb2e8bf3115c0519df97040c1542d5f936787a6db8a090e`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.3.0-source.zip`
