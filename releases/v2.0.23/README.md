# CT-BN-Compat v2.0.23

Purpose: diagnostic-only correction for Sponge Mixin annotation retention in the final packaged JAR.

## Change

The final packaged Mixin classes store `org.spongepowered.asm.mixin.Mixin` under `RuntimeInvisibleAnnotations`, matching Sponge Mixin's CLASS-retention annotation contract. Class-level `@Mixin` is no longer stored under `RuntimeVisibleAnnotations` for any class listed in `ct_bn_v2.mixins.json`.

## Verification

- Version: `2.0.23`
- Bootstrap marker: `version=2.0.23`
- All 13 configured Mixins have class-level `@Mixin` under `RuntimeInvisibleAnnotations`
- `ScreenRenderMixin.class` bytes differ from v2.0.22
- Exactly one `ScreenRenderMixin.class`
- Exactly one `ct_bn_v2.mixins.json`
- No `org/spongepowered/...` stub classes are bundled
- JEI ingredient-hover diagnostics are preserved
- No tooltip behavior modification was introduced

## Archived files

- JAR: `CT-BN-Compat-1.21.1-v2.0.23.jar`
- JAR SHA-256: `1fcff03c89b9c6dc54b812b0869f2521daf5f8e6c90edd35337f0eee80e94c5a`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.0.23-source.zip`
- Source SHA-256: `96a7dbc1b90408cabfcd394fdc4445add46bf37118dcd6069156464806704bab`
