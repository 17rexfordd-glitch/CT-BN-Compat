# CT-BN-Compat v2.2.4

Purpose: read-only diagnostic runtime proof build for ImmersiveUI curse-name obfuscation ownership during the actual JEI hover reproduction.

## Change

- Preserved ItemStack tooltip assembly, ItemEnchantments section/insertion, Enchantment.getFullname, JEI hover, RenderHelper, GuiGraphics, and ColorTooltips observations.
- Added exact-SHA optional tracing for ImmersiveUI 0.3.3 `EnchantmentMixin.obfuscateCursedEnchantments`.
- Added exact-SHA optional tracing for ImmersiveUI 0.3.3 `RenderUtils.obfuscate`, `RenderUtils.stylize`, and `RenderUtils.lambda$stylize$1`.
- Runtime ownership marker `firstObfuscatedOwner` is emitted only when the runtime chain proves where `obfuscated=true` first appears.
- Removed broad obsolete `UpstreamSubscriber01Mixin` through `UpstreamSubscriber30Mixin` diagnostics from the active mixin config and packaged JAR.
- No tooltip contents, cancellation, redirect, return-value override, ImmersiveUI disable, or ColorTooltips animation behavior change was introduced.

## Verification

- Version markers: `2.2.4`.
- `behavioral-fixes=false` remains present.
- `GETSTATIC NeoForge.EVENT_BUS` descriptor remains `Lnet/neoforged/bus/api/IEventBus;`.
- `GETFIELD AbstractContainerMenu.slots` descriptor remains `Lnet/minecraft/core/NonNullList;`.
- No `NeoForge$EventBus` reference remains.
- No `AbstractContainerMenu.slots:Ljava/util/List;` reference remains.
- No broad `UpstreamSubscriberXXMixin` entries remain in `ct_bn_v2.mixins.json`.
- No broad `UpstreamSubscriberXXMixin.class` files are packaged.
- All configured Mixins have packaged `@Mixin` metadata.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.
- Could not run Gradle here because the wrapper distribution could not be downloaded offline; final packaged classfiles/JAR were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.2.4.jar`
- SHA-256: `669f8003f53aa8c4398e846b3acca1c1a05ca6b2f036b3f55bbc648ce1fdccf7`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.4-source.zip`
