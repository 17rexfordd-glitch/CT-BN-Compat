# CT-BN-Compat Version Archive

This file tracks CT-BN-Compat builds produced during the ChatGPT-assisted Minecraft mod compatibility workflow.

## Current baseline

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
