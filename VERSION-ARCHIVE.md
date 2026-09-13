# CT-BN-Compat Version Archive

This file tracks CT-BN-Compat builds produced during the ChatGPT-assisted Minecraft mod compatibility workflow.

## Current baseline

### v2.1.0

- Built artifact: `CT-BN-Compat-1.21.1-v2.1.0.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.1.0-source.zip`
- SHA-256: `27e6c3b4cbcea8c7c40fee154d2dab3da99a3647525e3c9eb6b85ba3956f477d`
- Purpose: diagnostic-only senior audit and cleanup baseline before continuing runtime testing.
- Key verification: all 13 configured Mixins have CLASS-retention `@Mixin` in `RuntimeInvisibleAnnotations`; all packaged classes are Java 21/classfile major 65; known bad descriptors are absent; no reflection, stubs, duplicate ZIP entries, or normal runtime references into the Mixin package are present.
- Preserved: JEI IngredientGrid/JeiTooltip/RenderHelper diagnostic path, corrected NeoForge `@Mod.dist` metadata, external bridge architecture, and no behavioral tooltip modification.
- Notes: `DIAGNOSTIC-NOTES.txt` is rewritten as a concise v2.1.0 design/regression baseline.

### v2.0.25

- Built artifact: `CT-BN-Compat-1.21.1-v2.0.25.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.25-source.zip`
- SHA-256: `939f52c0c64b07fdb7e2c3b903bd8dd3a7fddb9aa4258fc1369864ed2e0d18f2`
- Purpose: diagnostic-only stale marker correction; updates packaged Mixin bootstrap and ACTIVE runtime markers to 2.0.25.
- Key verification: mod metadata, main ACTIVE marker, and Mixin bootstrap marker all report 2.0.25; no stale executable 2.0.23 or 2.0.24 marker remains in the final packaged JAR.
- Preserved: v2.0.24 corrected NeoForge `@Mod(dist=[Dist.CLIENT])` array metadata, v2.0.23 Mixin CLASS-retention annotations, JEI ingredient-hover diagnostics, JEI RenderHelper handoff marker, external bridge architecture, NeoForge/Minecraft descriptor fixes, and no behavioral tooltip modification.

### v2.0.24

- Built artifact: `CT-BN-Compat-1.21.1-v2.0.24.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.24-source.zip`
- SHA-256: `35380b5bababf83cebd15b620d6962104f16be83ada72b946024b098fce12638`
- Purpose: diagnostic-only startup fix correcting malformed NeoForge annotation metadata that could trip automatic subscriber/mod annotation scanning before the runtime anchor registered.
- Key verification: packaged `@Mod(dist=...)` now stores `dist` as a one-element `Dist[]` array value; no packaged automatic event-subscriber annotation metadata is present.
- Preserved: v2.0.23 Mixin CLASS-retention annotations, JEI ingredient-hover diagnostics, JEI RenderHelper handoff marker, external bridge architecture, NeoForge/Minecraft descriptor fixes, and no behavioral tooltip modification.

### v2.0.23

- Built artifact: `CT-BN-Compat-1.21.1-v2.0.23.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.23-source.zip`
- SHA-256: `1fcff03c89b9c6dc54b812b0869f2521daf5f8e6c90edd35337f0eee80e94c5a`
- Purpose: diagnostic-only startup fix correcting final packaged Sponge Mixin annotation retention.
- Key verification: every class listed in `ct_bn_v2.mixins.json` stores class-level `org.spongepowered.asm.mixin.Mixin` under `RuntimeInvisibleAnnotations`, not `RuntimeVisibleAnnotations`.
- Preserved: JEI ingredient-hover diagnostics, JEI RenderHelper handoff marker, external bridge architecture, NeoForge/Minecraft descriptor fixes, and no behavioral tooltip modification.
- Notes: source tree was recovered from compiled artifacts and conversation-retained files, not from an original historical Git checkout.

### v2.0.22

- Built artifact: `CT-BN-Compat-1.21.1-v2.0.22.jar`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.22-source.zip`
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
