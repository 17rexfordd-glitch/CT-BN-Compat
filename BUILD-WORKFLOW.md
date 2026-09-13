# CT-BN-Compat Build Workflow

Target environment:

- Minecraft 1.21.1
- NeoForge 21.1.248
- Java 21
- Modpack: ImmersiveRPG & MineColonies v0.4.0 beta1

Primary suspected compatibility surface:

- ColorTooltips 1211.3.4
- BetterNether 21.0.26
- SimplyTooltips 0.1.5
- JEI 19.44.0.403
- Better Advanced Tooltips 2101.1.0-build.5
- Icon Leading Tooltip 1.0.8
- ImmersiveUI 0.3.3

Chat workflow:

1. Builder chat creates the next CT-BN-Compat build.
2. Pre-mod tester chat inspects the compiled JAR before runtime testing.
3. Post-mod tester chat analyzes runtime logs after testing.
4. Tester prompts are treated as authoritative input for the next builder iteration unless contradicted by hard technical evidence.

Packaging rule:

- Always inspect the final packaged JAR, not only source files.
- Verify Mixin annotations from compiled class metadata.
- Verify exact method/field descriptors against Minecraft/NeoForge/JEI runtime bytecode.
- Keep diagnostic-only builds free of tooltip behavior changes until the real owner/path is proven.

Repository rule:

- The repository is the durable reference going forward.
- Each new build should update source, version archive, and artifact metadata.


## v2.1.1 verification addendum

Before delivery, inspect the final packaged main class and confirm `GETSTATIC net/neoforged/neoforge/common/NeoForge.EVENT_BUS:Lnet/neoforged/bus/api/IEventBus;`. Reject any build containing `NeoForge$EventBus`.


## v2.1.2 verification addendum

Before delivery, inspect the final packaged diagnostics classes and confirm any `GETFIELD net/minecraft/world/inventory/AbstractContainerMenu.slots` uses descriptor `Lnet/minecraft/core/NonNullList;`. Reject any build containing `AbstractContainerMenu.slots:Ljava/util/List;` in executable bytecode.


## v2.1.3 verification addendum — onFrameWithoutLiveItem dedupe

Before delivery, inspect the final packaged `DiagnosticLog.class` and confirm `COLORTOOLTIPS onFrameWithoutLiveItem()` diagnostic emission is deduplicated by entry/state-change with a heartbeat no faster than about five seconds. Confirm `behavioral-fixes=false` and no animation/state behavior modification.

## v2.2.1
Run stop, remove build, clean, then build --no-build-cache --rerun-tasks. The diagnosticTest task is part of check. Verify all prior injection selectors remain plus the new paired RETURN hooks and read-only text bridge. Test logical-state dedupe and distinguish first observed divergence from proven handler mutation. No behavior fix without gameplay evidence.

## v2.2.2
Upstream construction/event dispatch diagnostic boundaries, 30 optional exact-method handler observations, canonical component JSON fingerprints, and reduced downstream logging. No behavior fix. Clean build and static verification evidence in verification/. Historical releases and original reference dependencies retained.
