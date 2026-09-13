# CT-BN-Compat v2.1.2

Purpose: diagnostic-only descriptor correction for container slot inspection plus tooltip-owner log deduplication.

## Change

- Preserved the v2.1.1 NeoForge event-bus descriptor correction.
- Corrected final packaged references to `AbstractContainerMenu.slots` so the field descriptor is `Lnet/minecraft/core/NonNullList;`.
- Removed the bad `AbstractContainerMenu.slots:Ljava/util/List;` field descriptor from executable bytecode.
- Added tooltip-owner dedupe/rate limiting so identical startup-time owner diagnostics with `item=none` and `recentJeiHover=false` cannot flood the log.
- JEI startup-time `ItemTooltipEvent` ownership noise is not treated as the user's JEI ingredient hover.
- No tooltip behavior modification was introduced.

## Verification

- Version markers: `2.1.2`
- Main ACTIVE marker: `v2.1.2`
- Mixin bootstrap marker: `version=2.1.2`
- `GETFIELD AbstractContainerMenu.slots` descriptor is exactly `Lnet/minecraft/core/NonNullList;`.
- No `AbstractContainerMenu.slots:Ljava/util/List;` reference remains in executable bytecode.
- Correct `NeoForge.EVENT_BUS:Lnet/neoforged/bus/api/IEventBus;` remains intact.
- All 13 configured Mixins still have class-level `@Mixin` under `RuntimeInvisibleAnnotations`.
- Corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata remains intact.
- No `@EventBusSubscriber` annotation metadata is packaged.
- No `java/lang/reflect` references.
- No normal runtime class directly references `dev/devun/ctbncompat/mixin/`.
- No bundled dependency/stub classes.
- No duplicate ZIP entries.
- JEI IngredientGrid/JeiTooltip/RenderHelper diagnostics are preserved.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.1.2.jar`
- SHA-256: `d29a2f8ecd5a1f02e92fd2094d1a162ee44c04040877afa0808d17dbe671eb66`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.2-source.zip`