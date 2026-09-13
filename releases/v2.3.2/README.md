# CT-BN-Compat v2.3.2

Purpose: compatibility release with minimal proof detector restored from the v2.2.x diagnostic phase. The v2.3.1 compatibility fix itself is unchanged.

## Change

- Preserved the v2.3.1 BetterNether compatibility hook and stabilizer behavior unchanged.
- Restored only the minimum known-good detector from the v2.2.x `EnchantmentTrace` sampler/journal:
  - styled component traversal using `FormattedText.StyledContentConsumer`
  - `dark_red` detection
  - `obfuscated=true` detection
  - before/after stabilization component state capture
  - post-fix component SHA-256 hash comparison across frames
  - strong deduplication with an approximately five-second heartbeat
- Added a small `ItemStackDetectorMixin` only to bind the current tooltip item while `ItemStack.getTooltipLines(...)` runs.
- Detector scope is limited to `betternether:flaming_ruby_*` and `betternether:cincinnasite_pickaxe_diamond`.
- Enchantment scope is limited to `betternether:ruby_fire` and `betternether:obsidian_breaker`.
- Did not restore JEI chain dumps, RenderUtils helper tracing, BAT/Icon tracing, upstream owner tracing, or large component JSON dumps.
- Did not change tooltip contents beyond the existing v2.3.1 stabilization hook.

## Verification

- Version markers: `2.3.2`.
- `behavioral-fixes=true` remains present.
- `BetterNetherCurseNameStabilizer` compatibility logic is preserved.
- `ct_bn_v2.mixins.json` contains only `EnchantmentNameMixin` and `ItemStackDetectorMixin`.
- `EnchantmentNameMixin.@Inject` is runtime-visible.
- `ItemStackDetectorMixin.@Inject` is runtime-visible.
- `ColorTooltipsBetterNetherCompat.@Mod` is runtime-visible.
- `@Mixin` remains runtime-invisible on both mixin classes.
- Detector uses `Component.visit(FormattedText$StyledContentConsumer, Style)`; no `Component.visit(BiFunction, Style)` call remains.
- Item ID lookup uses `BuiltInRegistries.ITEM:Lnet/minecraft/core/DefaultedRegistry;` and `DefaultedRegistry.getKey(Object)`.
- No `EnchantmentTags.CURSE:Object` regression.
- No `EnchantmentTrace`, `TooltipTrace`, `UpstreamTrace`, `ImmersiveEnchantmentStyleMixin`, BAT/Icon/JEI diagnostic mixins, or broad `UpstreamSubscriberXXMixin` classes are packaged.
- No tooltip rendering cancellation, tooltip-list replacement, animator reset redirect, or same-item redirect is packaged.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.2.jar`
- SHA-256: `f241bc7711b2fdf2338568a2bf9878732743b360eddbe1fc5730afc4f21505b8`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.3.2-source.zip`
