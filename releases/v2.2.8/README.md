# CT-BN-Compat v2.2.8

Purpose: read-only diagnostic regression fix after v2.2.7. v2.2.7 reached the BetterNether JEI tooltip path again, but `EnchantmentTrace.sample` invoked a nonexistent Minecraft 1.21.1 runtime descriptor: `Component.visit(BiFunction, Style)`.

## Change

- Corrected the compile stub/API descriptor for styled text traversal to `Component.visit(FormattedText.StyledContentConsumer, Style)`.
- Recompiled `EnchantmentTrace.class` so sampling uses the Minecraft 1.21.1 `FormattedText$StyledContentConsumer` descriptor, not `java.util.function.BiFunction`.
- Added a defensive, read-only sampling failure log: `ENCHANTMENT-SAMPLE-FAILED ... action=continue-original-tooltip`.
- Kept the focused JEI, ItemStack tooltip-generation, `Enchantment.getFullname`, ImmersiveUI 0.3.3, `RenderUtils.obfuscate`, `RenderUtils.stylize`, and `RenderUtils.lambda$stylize$1` tracing.
- Continued focusing runtime proof on `betternether:ruby_fire` and `betternether:obsidian_breaker` without hard-coding the compatibility target to one item ID.
- Did not modify tooltip contents, cancellation, redirects, return values, ImmersiveUI formatting, or ColorTooltips animation behavior.

## Verification

- Version markers: `2.2.8`.
- `behavioral-fixes=false` remains present.
- No `Component.visit:(Ljava/util/function/BiFunction;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;` invocation remains.
- Packaged `EnchantmentTrace.class` invokes `Component.visit:(Lnet/minecraft/network/chat/FormattedText$StyledContentConsumer;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;`.
- No `NoSuchFieldError` regression: `EnchantmentTags.CURSE:Lnet/minecraft/tags/TagKey;` remains present.
- Focused ImmersiveUI tracing remains packaged.
- `ImmersiveEnchantmentStyleMixin` remains in the active Mixin configuration.
- No tooltip cancellation or mutation code was introduced.
- Broad obsolete `UpstreamSubscriberXXMixin` diagnostics remain absent.
- `GETSTATIC NeoForge.EVENT_BUS` descriptor remains `Lnet/neoforged/bus/api/IEventBus;`.
- `GETFIELD AbstractContainerMenu.slots` descriptor remains `Lnet/minecraft/core/NonNullList;`.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.2.8.jar`
- SHA-256: `fb3bf5af506aff68123f7cd8a66454e22258142f6d6ce444e40587fd5bc14059`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.8-source.zip`