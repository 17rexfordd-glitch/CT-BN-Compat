# CT-BN-Compat v2.1.0

Purpose: diagnostic-only senior audit and cleanup baseline before continuing JEI ingredient-hover runtime testing.

## Scope

- Kept the corrected v2.0.24 NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata.
- Kept the v2.0.23 Sponge Mixin CLASS-retention annotation fix.
- Revalidated the packaged Mixin list, known bad descriptors, class versions, duplicate entries, bootstrap/runtime architecture, and JEI 19.44.0.403 instrumentation path.
- Rewrote `DIAGNOSTIC-NOTES.txt` as a concise v2.1.0 baseline instead of an appended patch log.
- No tooltip behavior modification was introduced.

## Verification

- Version markers: `2.1.0`
- All configured Mixins have class-level `@Mixin` under `RuntimeInvisibleAnnotations`.
- All packaged classes are Java 21 / classfile major version 65.
- Known bad descriptors are absent.
- No `java/lang/reflect` references.
- No normal runtime class directly references `dev/devun/ctbncompat/mixin/`.
- No bundled dependency/stub classes.
- No duplicate ZIP entries.
- JEI IngredientGrid, six-argument `JeiTooltip.draw`, and NeoForge JEI `RenderHelper.renderTooltip` hooks remain present.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.1.0.jar`
- SHA-256: `27e6c3b4cbcea8c7c40fee154d2dab3da99a3647525e3c9eb6b85ba3956f477d`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.0-source.zip`
- Source ZIP SHA-256: `91296c62596ec5751ba2a90a0483219fe86cdde31ee273b8f85c2fed48d4b442`
