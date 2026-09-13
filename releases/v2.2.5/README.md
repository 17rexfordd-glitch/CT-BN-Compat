# CT-BN-Compat v2.2.5

Purpose: read-only diagnostic safety correction after v2.2.4; removes the unsafe ordinary Mixin target against ImmersiveUI's own Mixin implementation class while preserving transformed Enchantment and RenderUtils runtime tracing.

## Change

- Removed `ImmersiveEnchantmentMixin` from the active mixin config and final packaged JAR.
- Removed normal/runtime references to `it.hurts.octostudios.immersiveui.mixin.EnchantmentMixin`.
- Kept curse-path evidence collection on the real transformed `net.minecraft.world.item.enchantment.Enchantment.getFullname(...)` path.
- Kept exact-SHA gated `ImmersiveEnchantmentStyleMixin` tracing for `RenderUtils.obfuscate`, `RenderUtils.stylize`, and `RenderUtils.lambda$stylize$1`.
- Did not add MixinSquared or a new dependency.
- Did not alter tooltip contents, cancellation, redirects, return values, ImmersiveUI formatting, or ColorTooltips animation behavior.

## Verification

- Version markers: `2.2.5`.
- `behavioral-fixes=false` remains present.
- No mixin targets `it.hurts.octostudios.immersiveui.mixin.EnchantmentMixin`.
- No normal/runtime class references `it.hurts.octostudios.immersiveui.mixin.EnchantmentMixin`.
- `GETSTATIC NeoForge.EVENT_BUS` descriptor remains `Lnet/neoforged/bus/api/IEventBus;`.
- `GETFIELD AbstractContainerMenu.slots` descriptor remains `Lnet/minecraft/core/NonNullList;`.
- No `NeoForge$EventBus` reference remains.
- No `AbstractContainerMenu.slots:Ljava/util/List;` reference remains.
- Broad obsolete `UpstreamSubscriberXXMixin` diagnostics remain absent.
- JEI diagnostics and enchantment diagnostics remain packaged.
- All configured Mixins have packaged `@Mixin` metadata.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.
- Could not run Gradle here because the wrapper distribution could not be downloaded offline; final packaged classfiles/JAR were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.2.5.jar`
- SHA-256: `515eaccb3528e9b21353650909b14217c86a95c6d340a9e7fd206f2cf44f0e5f`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.5-source.zip`