# CT-BN-Compat v2.1.1

Purpose: diagnostic-only descriptor correction for explicit NeoForge runtime event-bus registration.

## Change

- Preserved the v2.1.0 senior-audit diagnostic baseline.
- Kept explicit runtime client-tick registration, but corrected the packaged `NeoForge.EVENT_BUS` field and listener call descriptors.
- The final packaged main class now references `net/neoforged/neoforge/common/NeoForge.EVENT_BUS:Lnet/neoforged/bus/api/IEventBus;`.
- Removed the bad `net/neoforged/neoforge/common/NeoForge$EventBus` descriptor from executable bytecode.
- No automatic event-subscriber path was reintroduced.
- No tooltip behavior modification was introduced.

## Verification

- Version markers: `2.1.1`
- Main ACTIVE marker: `v2.1.1`
- Mixin bootstrap marker: `version=2.1.1`
- `GETSTATIC NeoForge.EVENT_BUS` descriptor is exactly `Lnet/neoforged/bus/api/IEventBus;`.
- Listener registration invokes `net/neoforged/bus/api/IEventBus.addListener(Ljava/util/function/Consumer;)V`.
- No `NeoForge$EventBus` reference remains.
- All 13 configured Mixins still have class-level `@Mixin` under `RuntimeInvisibleAnnotations`.
- Corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata remains intact.
- No `@EventBusSubscriber` annotation metadata is packaged.
- No `java/lang/reflect` references.
- No normal runtime class directly references `dev/devun/ctbncompat/mixin/`.
- No bundled dependency/stub classes.
- No duplicate ZIP entries.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.1.1.jar`
- SHA-256: `0924a81ba7a8ac8401ddf403dc7f05184e938f214c6b4cd674c4a475bbb51c21`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.1-source.zip`
