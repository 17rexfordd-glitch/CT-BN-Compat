# CT-BN-Compat v2.0.25

Purpose: diagnostic-only stale version marker correction after the v2.0.24 NeoForge annotation metadata fix.

## Change

- Updated the final packaged `DiagnosticMixinPlugin.class` bootstrap marker to `version=2.0.25`.
- Updated the main diagnostics ACTIVE marker to `v2.0.25`.
- Kept the v2.0.24 corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata.
- Kept Sponge Mixin `@Mixin` metadata under `RuntimeInvisibleAnnotations`.
- No tooltip behavior modification was introduced.

## Verification

- Mod metadata: `2.0.25`
- Main ACTIVE marker: `v2.0.25`
- Mixin bootstrap marker: `version=2.0.25`
- No stale executable `2.0.23` or `2.0.24` marker remains in the final packaged JAR.
- All 13 configured Mixins have class-level `@Mixin` under `RuntimeInvisibleAnnotations`, not `RuntimeVisibleAnnotations`.
- Corrected `@Mod(dist=[Dist.CLIENT])` metadata remains intact.
- JEI ingredient-hover diagnostics are preserved.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.0.25.jar`
- SHA-256: `939f52c0c64b07fdb7e2c3b903bd8dd3a7fddb9aa4258fc1369864ed2e0d18f2`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.25-source.zip`
