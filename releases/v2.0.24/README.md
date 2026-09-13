# CT-BN-Compat v2.0.24

Diagnostic-only correction for malformed NeoForge annotation metadata that caused startup to fail before runtime diagnostics registered.

## Included files

- `CT-BN-Compat-1.21.1-v2.0.24.jar`
- `CT-BN-Compat-1.21.1-v2.0.24-source.zip`

## SHA-256

- JAR: `35380b5bababf83cebd15b620d6962104f16be83ada72b946024b098fce12638`
- Source ZIP: `dc9643aec30ee679b4bbafb1c0d1e45ab81f92f99475a4dfbc23e82e25b4b69f`

## Notes

- Packaged `@Mod(dist=...)` now stores the value as a `Dist[]` array.
- No packaged automatic event-subscriber annotations are present.
- Explicit `NeoForge.EVENT_BUS.addListener(ClientRuntimeDiagnostics::onClientTick)` remains the runtime diagnostic path.
- No tooltip behavior fix was introduced.
