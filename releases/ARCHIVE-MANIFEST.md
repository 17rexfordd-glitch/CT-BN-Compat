# CT-BN-Compat Archive Manifest

Rolling project archive updated through v2.1.0. It contains CT-BN-Compat release artifacts plus external reference mod JARs needed for future compatibility/debugging work.

## Latest CT-BN build

- `releases/v2.1.0/CT-BN-Compat-1.21.1-v2.1.0.jar`
- `releases/v2.1.0/CT-BN-Compat-1.21.1-v2.1.0-source.zip`

## External reference dependencies

- `reference-dependencies/colortooltips-1211.3.4.jar` — `e60eec92082cbd71bdbc5f319f75ce9c0982f544d5a0ed21a56e01df09d187a0`
- `reference-dependencies/ImmersiveUI-NEOFORGE-0.3.3+1.21.1.jar` — `6b914e305f27962a91d49a659b77eb65f47acee9d79930801b4fc1d3e474c8a4`
- `reference-dependencies/SimplyTooltips-neoforge-0.1.5.jar` — `fdac367da1c5243ac5225f00f831b786817452ee9fa6a7f4acd99ec819b0f2b7`
- `reference-dependencies/better-advanced-tooltips-2101.1.0-build.5.jar` — `0f76d46f2f18eb67e67c7974170d7b27deb66473396461c8dc50d6949de1449b`
- `reference-dependencies/icon-leading-tooltip-neoforge-1.0.8.jar` — `e3c4053104128ecf2f6b4b5600ca615f5474f1e819898b0aa7b2836b6dcb6adb`
- `reference-dependencies/jei-1.21.1-neoforge-19.44.0.403.jar` — `31890ccbaf93cf90e2816b8f7cfb5a1340fe20bd8a1680a84e0050f7e07769bd`

## v2.1.1

- `releases/v2.1.1/CT-BN-Compat-1.21.1-v2.1.1.jar`
- `releases/v2.1.1/CT-BN-Compat-1.21.1-v2.1.1-source.zip`
- SHA-256: `0924a81ba7a8ac8401ddf403dc7f05184e938f214c6b4cd674c4a475bbb51c21`
- Note: diagnostic-only fix for `NeoForge.EVENT_BUS` descriptor; expected descriptor is `Lnet/neoforged/bus/api/IEventBus;`.

## v2.1.2

- `releases/v2.1.2/CT-BN-Compat-1.21.1-v2.1.2.jar`
- `releases/v2.1.2/CT-BN-Compat-1.21.1-v2.1.2-source.zip`
- SHA-256: `d29a2f8ecd5a1f02e92fd2094d1a162ee44c04040877afa0808d17dbe671eb66`
- Note: diagnostic-only fix for `AbstractContainerMenu.slots` descriptor and tooltip-owner log dedupe.

## v2.1.3

- `releases/v2.1.3/CT-BN-Compat-1.21.1-v2.1.3.jar`
- `releases/v2.1.3/CT-BN-Compat-1.21.1-v2.1.3-source.zip`
- SHA-256: `89fe544a3487b39963c40c79524d9cd8b2c3f0dd20455140b82e11c4570d9952`
- Note: diagnostic-only dedupe for repeated `COLORTOOLTIPS onFrameWithoutLiveItem()` logs; no behavior fix.

## v2.2.0
Added releases/v2.2.0 JAR, source ZIP, and README; preserved all supplied earlier release files and dependency references. Original uploaded v2.1.3 source ZIP retained under releases/v2.1.3. The v2.2.0 source ZIP excludes releases; the rolling archive carries the full history.


## v2.2.1
Diagnostic-only mutation tracing and logical-state dedupe; real clean source build. All supplied release history and references retained. See releases/v2.2.1 and verification/.

## v2.2.2
Upstream construction/event dispatch diagnostic boundaries, 30 optional exact-method handler observations, canonical component JSON fingerprints, and reduced downstream logging. No behavior fix. Clean build and static verification evidence in verification/. Historical releases and original reference dependencies retained.
