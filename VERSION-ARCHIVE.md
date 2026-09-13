# CT-BN-Compat Version Archive

This file tracks CT-BN-Compat builds produced during the ChatGPT-assisted Minecraft mod compatibility workflow.

## Current baseline

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
