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
