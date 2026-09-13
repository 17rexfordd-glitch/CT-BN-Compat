# CT-BN-Compat v2.3.1

Purpose: packaging/annotation-retention correction for the v2.3.0 compatibility release. The BetterNether compatibility behavior is unchanged.

## Change

- Kept the v2.3.0 compatibility logic unchanged.
- Corrected packaged annotation retention:
  - `EnchantmentNameMixin.@Inject` is now under `RuntimeVisibleAnnotations`.
  - `ColorTooltipsBetterNetherCompat.@Mod` is now under `RuntimeVisibleAnnotations`.
  - `EnchantmentNameMixin.@Mixin` remains under `RuntimeInvisibleAnnotations`.
- Updated version markers to `2.3.1`.
- Did not change the BetterNether enchantment-name stabilization behavior.

## Verification

- Version markers: `2.3.1`.
- `behavioral-fixes=true` remains present.
- `EnchantmentNameMixin.class` has `@Inject` under `RuntimeVisibleAnnotations`.
- `ColorTooltipsBetterNetherCompat.class` has `@Mod` under `RuntimeVisibleAnnotations`.
- `EnchantmentNameMixin.class` keeps `@Mixin` under `RuntimeInvisibleAnnotations`.
- The bytecode for the compatibility hook/stabilizer is unchanged from v2.3.0 apart from version text and annotation attributes.
- Final mixin config still contains only `EnchantmentNameMixin`.
- No v2.2.x diagnostic flood classes are packaged.
- No stale broken diagnostic calls remain: no `Component.visit(BiFunction, Style)`, no `EnchantmentTags.CURSE:Object`.
- No tooltip rendering cancellation, tooltip-list replacement, animator reset redirect, or same-item redirect is packaged.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.1.jar`
- SHA-256: `4a087d485a4f5290422683b4c0afadf1df25b6a3c2cd09c04fd3754d2867ba4f`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.3.1-source.zip`