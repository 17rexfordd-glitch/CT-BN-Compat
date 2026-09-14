# CT-BN-Compat v2.3.3

Purpose: canonicalize the final returned component for the two proven BetterNether curse enchantments while preserving every detector/tracer/observer/logging path restored from the working v2.2.8 base.

## Cause

v2.3.2 proved that the compatibility stabilizer applies and removes `obfuscated=true`, but the post-fix component still alternated between two structures: a normalized per-character component on frames where ImmersiveUI animated the curse name, and the normal translatable component on frames where it did not. ColorTooltips still saw different component trees/hashes and restarted its tooltip transition.

## Correction

- Kept every restored v2.2.8 detector, tracer, observer, diagnostic hook, and logging path.
- Changed only `BetterNetherCurseNameStabilizer`.
- Instead of recoloring the incoming component tree, the stabilizer now returns the same canonical component structure every invocation for:
  - `betternether:ruby_fire`
  - `betternether:obsidian_breaker`
- Canonical structure:
  - root: `holder.value().description().copy()`
  - style: `Style.EMPTY.withColor(ChatFormatting.RED).withObfuscated(Boolean.FALSE)` merged through `ComponentUtils.mergeStyles(...)`
  - level suffix: if vanilla would show a suffix, append `CommonComponents.SPACE` and `Component.translatable("enchantment.level." + level)`
- This preserves normal red styling, enchantment levels, and description lines, while avoiding per-character reconstruction.

## Verification

- Version markers: `2.3.3`.
- `behavioral-fixes=true` remains present.
- No detector was removed, disabled, simplified, replaced, reduced, cleaned up, or altered.
- v2.2.8 detector classes and mixins remain packaged, including `EnchantmentTrace`, `TooltipTrace`, JEI hooks, ItemStack hooks, Enchantment section/name hooks, and `ImmersiveEnchantmentStyleMixin`.
- `BetterNetherCurseNameStabilizer` remains packaged and uses `canonicalName(...)`.
- `BetterNetherCurseNameStabilizer.class` references `ComponentUtils.mergeStyles`, `CommonComponents.SPACE`, `Component.translatable`, and `Enchantment.getMaxLevel`.
- The stabilizer does not call `MutableComponent.getSiblings`, so it no longer preserves incoming per-character child structure.
- No stale broken diagnostic API calls remain: no `Component.visit(BiFunction, Style)`, no `EnchantmentTags.CURSE:Object`.
- No tooltip rendering cancellation, tooltip-list replacement, animator reset redirect, or same-item redirect is packaged.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, MixinExtras, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.3.jar`
- SHA-256: `4ef43f94c7d6cb47d2ccc10937b0d2c177ff227eeb5f896cfcc59fb942970d31`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.3.3-source.zip`
