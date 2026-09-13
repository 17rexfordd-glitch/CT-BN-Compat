# CT-BN-Compat v2.2.7

Purpose: read-only diagnostic regression fix after v2.2.6. v2.2.6 broke JEI tooltip generation because `EnchantmentTrace.nameEntry` was compiled against a bad compatibility stub where `EnchantmentTags.CURSE` was `Object` instead of Minecraft 1.21.1's `TagKey<Enchantment>` field.

## Change

- Corrected the compile stub/API descriptor for `net.minecraft.tags.EnchantmentTags.CURSE` to `Lnet/minecraft/tags/TagKey;`.
- Recompiled `EnchantmentTrace.class` so `Holder.is(...)` uses the `TagKey` descriptor, not `Object`.
- Kept the focused JEI, ItemStack tooltip-generation, `Enchantment.getFullname`, ImmersiveUI 0.3.3, `RenderUtils.obfuscate`, `RenderUtils.stylize`, and `RenderUtils.lambda$stylize$1` tracing.
- Added no try/catch masking for the descriptor issue; the underlying binary descriptor was fixed.
- Did not modify tooltip contents, cancellation, redirects, return values, ImmersiveUI formatting, or ColorTooltips animation behavior.

## Verification

- Version markers: `2.2.7`.
- `behavioral-fixes=false` remains present.
- No `EnchantmentTags.CURSE:Ljava/lang/Object;` reference remains.
- Packaged `EnchantmentTrace.class` references `EnchantmentTags.CURSE:Lnet/minecraft/tags/TagKey;`.
- Packaged `EnchantmentTrace.class` invokes `Holder.is:(Lnet/minecraft/tags/TagKey;)Z`.
- Focused ImmersiveUI tracing remains packaged.
- No tooltip cancellation or mutation code was introduced.
- Broad obsolete `UpstreamSubscriberXXMixin` diagnostics remain absent.
- `GETSTATIC NeoForge.EVENT_BUS` descriptor remains `Lnet/neoforged/bus/api/IEventBus;`.
- `GETFIELD AbstractContainerMenu.slots` descriptor remains `Lnet/minecraft/core/NonNullList;`.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.2.7.jar`
- SHA-256: `b6d1b58503a2c92aaf21a123553f12a1deaba29f829f6016b89cd604a7058a33`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.7-source.zip`
