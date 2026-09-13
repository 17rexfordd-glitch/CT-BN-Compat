# CT-BN-Compat Version Archive

This file tracks CT-BN-Compat builds produced during the ChatGPT-assisted Minecraft mod compatibility workflow.

## Current baseline

### v2.1.3

- Built artifact: `CT-BN-Compat-1.21.1-v2.1.3.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.3-source.zip`
- SHA-256: `89fe544a3487b39963c40c79524d9cd8b2c3f0dd20455140b82e11c4570d9952`
- Purpose: diagnostic-only ColorTooltips no-live-item log dedupe.
- Key verification: `COLORTOOLTIPS onFrameWithoutLiveItem()` now logs only on state entry, state changes, or a heartbeat no more than about once every 5 seconds; `NeoForge.EVENT_BUS:IEventBus` and `AbstractContainerMenu.slots:NonNullList` descriptors remain intact.
- Preserved: v2.1.2 slot descriptor correction, v2.1.1 event-bus descriptor correction, v2.1.0 audit baseline, JEI ingredient-hover diagnostics, JEI RenderHelper handoff marker, external bridge architecture, and no behavioral tooltip modification.

### v2.1.2

- Built artifact: `CT-BN-Compat-1.21.1-v2.1.2.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.2-source.zip`
- SHA-256: `d29a2f8ecd5a1f02e92fd2094d1a162ee44c04040877afa0808d17dbe671eb66`
- Purpose: diagnostic-only Minecraft descriptor correction and tooltip-owner log dedupe.
- Key verification: final packaged `GETFIELD AbstractContainerMenu.slots` uses descriptor `Lnet/minecraft/core/NonNullList;`; no `AbstractContainerMenu.slots:Ljava/util/List;` reference remains; `NeoForge.EVENT_BUS:IEventBus` remains intact.
- Preserved: v2.1.1 event-bus descriptor correction, v2.1.0 audit baseline, v2.0.24 corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata, v2.0.23 Mixin CLASS-retention annotations, JEI ingredient-hover diagnostics, JEI RenderHelper handoff marker, external bridge architecture, and no behavioral tooltip modification.

### v2.1.1

- Built artifact: `CT-BN-Compat-1.21.1-v2.1.1.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.1-source.zip`
- SHA-256: `0924a81ba7a8ac8401ddf403dc7f05184e938f214c6b4cd674c4a475bbb51c21`
- Purpose: diagnostic-only descriptor correction for explicit NeoForge runtime event-bus registration.
- Key verification: final packaged `GETSTATIC NeoForge.EVENT_BUS` uses descriptor `Lnet/neoforged/bus/api/IEventBus;`; no `NeoForge$EventBus` reference remains; automatic subscriber registration is still absent.
- Preserved: v2.1.0 audit baseline, v2.0.24 corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata, v2.0.23 Mixin CLASS-retention annotations, JEI ingredient-hover diagnostics, JEI RenderHelper handoff marker, external bridge architecture, and no behavioral tooltip modification.

### v2.1.0

- Built artifact: `CT-BN-Compat-1.21.1-v2.1.0.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.0-source.zip`
- SHA-256: `27e6c3b4cbcea8c7c40fee154d2dab3da99a3647525e3c9eb6b85ba3956f477d`
- Purpose: diagnostic-only senior audit and cleanup baseline; verifies packaged Mixin metadata, known descriptors, bootstrap/runtime architecture, and JEI ingredient-hover instrumentation before further runtime testing.
- Key verification: all configured Mixins use CLASS-retention `@Mixin`, all classes are Java 21, no known bad descriptors/reflection/runtime references into the Mixin package/stubs/duplicate ZIP entries are present, and `DIAGNOSTIC-NOTES.txt` is rewritten as a concise v2.1.0 design document.
- Preserved: JEI IngredientGrid/JeiTooltip/RenderHelper diagnostics, corrected NeoForge `@Mod.dist` metadata, external bridge architecture, and no behavioral tooltip modification.


### v2.0.25

- Built artifact: `CT-BN-Compat-1.21.1-v2.0.25.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.25-source.zip`
- SHA-256: `939f52c0c64b07fdb7e2c3b903bd8dd3a7fddb9aa4258fc1369864ed2e0d18f2`
- Purpose: diagnostic-only stale marker correction; updates packaged Mixin bootstrap and ACTIVE runtime markers to 2.0.25.
- Preserved: v2.0.24 corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata, Sponge Mixin CLASS-retention annotations, JEI ingredient-hover diagnostics, and no behavioral tooltip modification.


### v2.0.23

- Built artifact: `CT-BN-Compat-1.21.1-v2.0.23.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.23-source.zip`
- SHA-256: `7a3ab5e7d9fd9e85ab3eca7daa5704aa7ccfbe0bbbbbea876c466c82696c93e0`
- Purpose: diagnostic-only build preserving JEI ingredient-hover instrumentation and fixing the packaged `ScreenRenderMixin` Mixin annotation issue.
- Notes: source tree was recovered from compiled artifacts and conversation-retained files, not from an original historical Git checkout.

## Rule going forward

Every new version built after this repository was created should add:

1. the updated source tree or source snapshot,
2. the built JAR filename,
3. the SHA-256 hash,
4. a short change summary,
5. whether the build is diagnostic-only or includes a behavioral fix.


### v2.0.23

- Built artifact: `releases/v2.0.23/CT-BN-Compat-1.21.1-v2.0.23.jar`
- Source snapshot: `releases/v2.0.23/CT-BN-Compat-1.21.1-v2.0.23-source.zip`
- JAR SHA-256: `1fcff03c89b9c6dc54b812b0869f2521daf5f8e6c90edd35337f0eee80e94c5a`
- Source SHA-256: `96a7dbc1b90408cabfcd394fdc4445add46bf37118dcd6069156464806704bab`
- Purpose: diagnostic-only correction for Sponge Mixin annotation retention in the final packaged JAR.
- Verification: all 13 configured Mixins store class-level `@Mixin` under `RuntimeInvisibleAnnotations`; no bundled `org/spongepowered/...` stubs; JEI diagnostics preserved.

# CT-BN-Compat v2.2.0

Diagnostic-only update from the hash-verified v2.1.3 baseline. Unchanged BetterNether RUNTIME-HOVER records now use a five-second heartbeat; changed hover state is immediate. Version markers are synchronized. Clean compilation with real dependencies corrects the old registry lookup invocation and emits correct Inject.at arrays.

All prior descriptor fixes, 13 Mixins, JEI hooks, runtime anchor, and existing dedupe are preserved. No tooltip/animation behavior fix. Clean build and packaged-bytecode validation passed; no in-game launch was performed.

JAR SHA-256: b0451eb08ee9e8182ff4239153caf999e723fe9838db88ecbe0e1aadc1f25a5c

See ../../verification/VERIFICATION.md and ../../README.md for evidence and reproduction instructions.

## v2.2.1
Diagnostic-only mutation tracing and logical-state dedupe; real clean source build. All supplied release history and references retained. See releases/v2.2.1 and verification/.

## v2.2.2
Upstream construction/event dispatch diagnostic boundaries, 30 optional exact-method handler observations, canonical component JSON fingerprints, and reduced downstream logging. No behavior fix. Clean build and static verification evidence in verification/. Historical releases and original reference dependencies retained.
