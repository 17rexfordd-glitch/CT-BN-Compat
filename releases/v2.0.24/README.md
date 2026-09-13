# CT-BN-Compat v2.0.24

Purpose: diagnostic-only correction for malformed NeoForge annotation metadata that caused startup to fail before runtime diagnostics registered.

## Change

- Packaged `@Mod(dist=...)` metadata now stores the client side value as a one-element `Dist[]` array instead of a scalar enum value.
- The source annotation uses explicit array syntax: `dist = {Dist.CLIENT}` so future source builds fail fast if a wrong local/stub annotation API is used.
- The final JAR was audited for packaged automatic event-subscriber annotation metadata; none is present.
- Explicit `NeoForge.EVENT_BUS.addListener(ClientRuntimeDiagnostics::onClientTick)` remains the runtime diagnostic registration path.

## Verification

- Version: `2.0.24`
- Bootstrap marker: `version=2.0.24`
- `@Mod` dist annotation value is stored as `[e#Dist.CLIENT]`
- No packaged automatic event-subscriber annotation metadata
- All 13 configured Mixins still have class-level `@Mixin` under `RuntimeInvisibleAnnotations`
- JEI ingredient-grid, six-argument `JeiTooltip`, and `JeiRenderHelper` diagnostics are preserved
- No bad `IModInfo#getVersion`, `BuiltInRegistries.ITEM`, or `ItemStack#getComponents` descriptors returned
- No tooltip behavior modification was introduced

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.0.24.jar`
- SHA-256: `35380b5bababf83cebd15b620d6962104f16be83ada72b946024b098fce12638`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.24-source.zip`
