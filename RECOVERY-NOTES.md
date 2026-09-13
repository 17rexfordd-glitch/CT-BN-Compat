# Source recovery notes

## Provenance

There was no GitHub repository or preserved Git checkout for this mod. This source tree was reconstructed on 2026-09-13 from the exact compiled v2.0.17 JAR and retained build artifacts from the same ChatGPT development session.

Reference compiled artifact:
- `CT-BN-Compat-1.21.1-v2.0.17.jar`
- SHA-256: `d9fcc82c01e979d63997a9e2b17fdddba2f75451817d6a8242bb43aec612b55c`

## Recovery rules

- Package/class names, method descriptors, Mixin targets, Mixin injection descriptors, diagnostic constants, bridge architecture, optional-mod expected versions, and v2.0.17 runtime guard behavior were recovered from final bytecode/resources.
- `RuntimeMetadataGuard.java` was available as retained source and copied directly.
- The remaining Java files were reconstructed into readable Java from the final v2.0.17 bytecode/disassembly.
- This is therefore a source-equivalent recovery base, not a claim that every whitespace/local-variable/source-expression choice matches the historical pre-compilation source exactly.

## Critical architecture to preserve

- Defined Mixin package: `dev.devun.ctbncompat.mixin`
- Runtime bridge: `dev.devun.ctbncompat.bridge.AbstractContainerScreenBridge`
- No normal runtime class may directly class-load/type-reference a class from the defined Mixin package.
- Optional Mixin PREPARE gating stays class-resource-only and must not call `ModList`.
- Runtime `ModList` / `IModInfo` verification occurs only from the deferred client runtime path.
- Runtime metadata reporting is nonfatal to live screen/container/hover diagnostics.
- This branch is diagnostic-only; no tooltip/animation behavioral fix is present.


## v2.1.1

Runtime construction failure fixed at descriptor level: `NeoForge.EVENT_BUS` now compiles/packages as `Lnet/neoforged/bus/api/IEventBus;`, not a fake nested `NeoForge$EventBus` type.


## v2.1.2

Runtime inventory-open failure fixed at descriptor level: `AbstractContainerMenu.slots` now packages as `Lnet/minecraft/core/NonNullList;`, not `Ljava/util/List;`. Tooltip-owner diagnostics are deduped/rate-limited for identical startup noise.


## v2.1.3

Runtime/logging cleanup: repeated unchanged `COLORTOOLTIPS onFrameWithoutLiveItem()` records now log only on state entry, captured-state change, or an about-5-second heartbeat. Descriptor fixes from v2.1.1 and v2.1.2 remain preserved.

## v2.2.0
Rebuilt the supplied v2.1.3 source with real Java 21 / NeoForge dependencies. See verification/VERIFICATION.md for the baseline comparison, compiler corrections, and diagnostic-only change. Historical recovery notes above are preserved.


## v2.2.1
Diagnostic-only mutation tracing and logical-state dedupe; real clean source build. All supplied release history and references retained. See releases/v2.2.1 and verification/.

## v2.2.2
Upstream construction/event dispatch diagnostic boundaries, 30 optional exact-method handler observations, canonical component JSON fingerprints, and reduced downstream logging. No behavior fix. Clean build and static verification evidence in verification/. Historical releases and original reference dependencies retained.
