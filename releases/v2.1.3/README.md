# CT-BN-Compat v2.1.3

Purpose: diagnostic-only cleanup for repeated ColorTooltips no-live-item frame diagnostics.

## Change

- Preserved the v2.1.2 Minecraft `AbstractContainerMenu.slots` NonNullList descriptor fix.
- Preserved the v2.1.1 NeoForge `NeoForge.EVENT_BUS:IEventBus` descriptor fix.
- Preserved the v2.1.0 audit baseline and all JEI instrumentation hooks.
- Added dedupe/rate limiting for repeated `COLORTOOLTIPS onFrameWithoutLiveItem()` diagnostics so unchanged per-frame state cannot spam logs.
- Preserved useful `reset()` / `ANIMATOR=reset` transition diagnostics while suppressing exact repeated unchanged transition records.
- No ColorTooltips animation/state behavior was changed.
- No tooltip behavior modification was introduced.

## Verification

- Version markers: `2.1.3`
- Main ACTIVE marker: `v2.1.3`
- Mixin bootstrap marker: `version=2.1.3`
- `GETSTATIC NeoForge.EVENT_BUS` descriptor remains `Lnet/neoforged/bus/api/IEventBus;`.
- `GETFIELD AbstractContainerMenu.slots` descriptor remains `Lnet/minecraft/core/NonNullList;`.
- No bad `AbstractContainerMenu.slots:Ljava/util/List;` reference remains.
- No `NeoForge$EventBus` reference remains.
- All 13 configured Mixins still have class-level `@Mixin` under `RuntimeInvisibleAnnotations`.
- Corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata remains intact.
- No `@EventBusSubscriber` annotation metadata is packaged.
- No `java/lang/reflect` references.
- No normal runtime class directly references `dev/devun/ctbncompat/mixin/`.
- No bundled dependency/stub classes.
- No duplicate ZIP entries.
- JEI IngredientGrid/JeiTooltip/RenderHelper diagnostics are preserved.
- `behavioral-fixes=false` remains in the runtime marker.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.1.3.jar`
- SHA-256: `89fe544a3487b39963c40c79524d9cd8b2c3f0dd20455140b82e11c4570d9952`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.3-source.zip`
