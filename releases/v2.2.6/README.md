# CT-BN-Compat v2.2.6

Purpose: read-only diagnostic correction after v2.2.5 runtime testing. v2.2.5 loaded successfully and reached the real JEI hover, but helper/runtime verification reported the exact-SHA RenderUtils target as invalid and the focused enchantment markers did not fire during the actual BetterNether JEI hover.

## Change

- Preserved the successful v2.2.5 startup, runtime anchor, ACTIVE-CONTAINER, JEI hover detection, logical ItemStack identity, exact SHA gating, and behavioral-fixes=false.
- Fixed the exact-SHA helper bootstrap record so `ImmersiveEnchantmentStyleMixin` records `RESOURCE_VERIFIED` instead of later appearing as a CHECK_FAILED/INVALID runtime verifier result.
- Kept the helper observer targeted at the normal runtime class `it.hurts.octostudios.immersiveui.util.RenderUtils`, not ImmersiveUI's Mixin implementation class.
- Loosened enchantment trace activation so the focused target enchantment diagnostics can record during a recent real JEI hover even when the ItemStack/list insertion pairing boundary is unavailable.
- Added focused immediate markers for `ENCHANTMENT-NAME-BEFORE`, `ENCHANTMENT-NAME-AFTER`, `ENCHANTMENT-LIST-INSERT`, and `IMMERSIVEUI-HELPER-EXECUTED`.
- Added fallback ownership reporting at `Enchantment.getFullname(...)` return while keeping the more precise helper ownership marker when `lambda$stylize$1.RETURN` proves the obfuscated transition.
- Did not restore broad `UpstreamSubscriberXXMixin` tracing.
- Did not modify tooltip contents, cancellation, redirects, return values, ImmersiveUI formatting, or ColorTooltips animation behavior.

## Verification

- Version markers: `2.2.6`.
- `behavioral-fixes=false` remains present.
- `ImmersiveEnchantmentStyleMixin` remains present in `ct_bn_v2.mixins.json` and packaged in the JAR.
- RenderUtils exact SHA gate remains for `9fa71307ef08ea14585e3cd9c43607121c522387fcea56b146e3a625f4471e4d`.
- The packaged helper descriptor targets remain `RenderUtils.obfuscate`, `RenderUtils.stylize`, and `RenderUtils.lambda$stylize$1`.
- Fallback `EnchantmentNameMixin` remains packaged.
- No mixin targets `it.hurts.octostudios.immersiveui.mixin.EnchantmentMixin`.
- No broad `UpstreamSubscriberXXMixin` entries or classes are packaged.
- `GETSTATIC NeoForge.EVENT_BUS` descriptor remains `Lnet/neoforged/bus/api/IEventBus;`.
- `GETFIELD AbstractContainerMenu.slots` descriptor remains `Lnet/minecraft/core/NonNullList;`.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.
- Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.2.6.jar`
- SHA-256: `5c172f8ffbce61c0ef346c2c954bfd6341edbafe034a430f12d76e4dfa93dca0`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.6-source.zip`
