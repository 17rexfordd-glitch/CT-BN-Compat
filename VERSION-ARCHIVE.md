# CT-BN-Compat Version Archive

This file tracks CT-BN-Compat builds produced during the ChatGPT-assisted Minecraft mod compatibility workflow.

## Current baseline

### v2.2.5

- Built artifact: `CT-BN-Compat-1.21.1-v2.2.5.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.5-source.zip`
- SHA-256: `515eaccb3528e9b21353650909b14217c86a95c6d340a9e7fd206f2cf44f0e5f`
- Purpose: read-only diagnostic Mixin-target safety correction after v2.2.4.
- Key verification: removed `ImmersiveEnchantmentMixin` direct targeting of ImmersiveUI's own Mixin implementation class; the final packaged JAR contains no `it.hurts.octostudios.immersiveui.mixin.EnchantmentMixin` class references; RenderUtils tracing remains exact-SHA gated; curse-path evidence is collected from the transformed `Enchantment.getFullname(...)` and `RenderUtils` runtime path.
- Preserved: v2.2.4 RenderUtils tracing, JEI hover diagnostics, ItemStack/enchantment assembly diagnostics, descriptor fixes, dedupe fixes, and no behavioral tooltip modification.
- Notes: Gradle could not run in the sandbox because the wrapper distribution could not be downloaded offline; the final packaged JAR/classfiles were directly inspected.

### v2.2.4

- Built artifact: `CT-BN-Compat-1.21.1-v2.2.4.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.4-source.zip`
- SHA-256: `669f8003f53aa8c4398e846b3acca1c1a05ca6b2f036b3f55bbc648ce1fdccf7`
- Purpose: read-only diagnostic runtime proof build for ImmersiveUI curse-name obfuscation ownership during the actual JEI hover reproduction.
- Key verification: v2.2.4 metadata and `behavioral-fixes=false` are present; `NeoForge.EVENT_BUS:Lnet/neoforged/bus/api/IEventBus;` and `AbstractContainerMenu.slots:Lnet/minecraft/core/NonNullList;` remain intact; optional ImmersiveUI helper instrumentation is exact-SHA gated; broad `UpstreamSubscriber01Mixin` through `UpstreamSubscriber30Mixin` diagnostics are removed from the active mixin config and packaged JAR.
- Preserved: JEI ingredient hover detection, active-container diagnostics, ItemStack tooltip-generation boundary, ItemEnchantments section/insertion tracing, Enchantment.getFullname tracing, RenderHelper/GuiGraphics/ColorTooltips observations, tooltip-owner dedupe, no-live-item dedupe, and no tooltip behavior modification.

### v2.2.3

- Built artifact: `CT-BN-Compat-1.21.1-v2.2.3.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.2.3-source.zip`
- SHA-256: `2aa9e2590c1f447e451ee7e070987df9fef059bfd03a09f7ef752ec3a6d2427f`
- Purpose: diagnostic-only tracing for BetterNether enchantment tooltip component changes.
- Key verification: records ItemStack list binding, ItemEnchantments section/insertion, Enchantment name, and optional ImmersiveUI RenderUtils obfuscation/styling observations.
- Preserved: prior descriptor fixes, runtime/JEI diagnostics, dedupe behavior, and no behavioral tooltip modification.

### v2.2.2

- Purpose: diagnostic-only upstream construction/event dispatch boundaries, optional exact-method handler observations, canonical component JSON fingerprints, and reduced downstream logging.
- No behavior fix.

### v2.2.1

- Purpose: diagnostic-only mutation tracing and logical-state dedupe.
- No behavior fix.

### v2.2.0

- Purpose: diagnostic-only update from v2.1.3 with synchronized version markers and BetterNether runtime hover heartbeat refinement.
- No behavior fix.

### v2.1.3

- Built artifact: `CT-BN-Compat-1.21.1-v2.1.3.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.3-source.zip`
- SHA-256: `89fe544a3487b39963c40c79524d9cd8b2c3f0dd20455140b82e11c4570d9952`
- Purpose: diagnostic-only ColorTooltips no-live-item log dedupe.
- Key verification: `COLORTOOLTIPS onFrameWithoutLiveItem()` logs only on state entry, state changes, or a heartbeat no more than about once every 5 seconds; `NeoForge.EVENT_BUS:IEventBus` and `AbstractContainerMenu.slots:NonNullList` descriptors remain intact.
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
- Purpose: diagnostic-only senior audit and cleanup baseline.
- Key verification: all configured Mixins use CLASS-retention `@Mixin`, all classes are Java 21, no known bad descriptors/reflection/runtime references into the Mixin package/stubs/duplicate ZIP entries are present, and JEI ingredient-hover instrumentation is preserved.

## Rule going forward

Every new version built after this repository was created should add:

1. the updated source tree or source snapshot,
2. the built JAR filename,
3. the SHA-256 hash,
4. a short change summary,
5. whether the build is diagnostic-only or includes a behavioral fix.
