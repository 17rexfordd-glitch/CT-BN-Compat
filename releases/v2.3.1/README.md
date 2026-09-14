# CT-BN-Compat v2.3.1

Purpose: bootstrap crash fix for the restored-v2.2.8-based v2.3.0 compatibility build. The working v2.2.8 detector and the new BetterNether compatibility behavior are preserved.

## Change

- Fixed `EnchantmentNameMixin.ctbn$copied(...)` so the injector signature matches what Mixin accepts for the `Enchantment.getFullname(...)` / `ComponentUtils.mergeStyles(...)` injection point.
- Removed the invalid extra `MutableComponent` local parameter from `ctbn$copied(...)`.
- Removed the `@Local(index=2)` dependency from that injector.
- Added a no-local copied-boundary marker through `EnchantmentTrace.copiedBoundary(holder, level)` so the detector still records the mergeStyles boundary without unsafe local capture.
- Left the RETURN compatibility hook intact: it reads the returned component through `CallbackInfoReturnable#getReturnValue()`, calls `EnchantmentTrace.compatibilityReturn(...)`, conditionally sets the stabilized return value, then continues `EnchantmentTrace.nameReturn(...)`.
- Did not strip the v2.2.8 detector.
- Did not globally disable ImmersiveUI or ColorTooltips.
- Did not cancel tooltip rendering or replace tooltip lists.

## Verification

- Version markers: `2.3.1`.
- `behavioral-fixes=true` remains present.
- `EnchantmentNameMixin.ctbn$copied` descriptor is now `(Holder, int, CallbackInfoReturnable) -> void`.
- No `ctbn$copied(..., MutableComponent)` injector descriptor remains.
- `EnchantmentNameMixin.ctbn$nameReturn` obtains the returned component from `CallbackInfoReturnable#getReturnValue()`.
- `EnchantmentNameMixin.ctbn$nameReturn` still calls `EnchantmentTrace.compatibilityReturn(...)` and `CallbackInfoReturnable#setReturnValue(...)` when stabilization returns a copied component.
- v2.2.8 detector classes remain packaged: `EnchantmentTrace`, `TooltipTrace`, JEI hooks, ItemStack hooks, Enchantment section/name hooks, and `ImmersiveEnchantmentStyleMixin`.
- `BetterNetherCurseNameStabilizer` remains packaged.
- No stale broken diagnostic API calls remain: no `Component.visit(BiFunction, Style)`, no `EnchantmentTags.CURSE:Object`.
- `@Inject` annotations are runtime-visible and `@Mixin` remains runtime-invisible.
- NeoForge `@Mod` is runtime-visible.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, MixinExtras, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.1.jar`
- SHA-256: `de7065dae7a20ac5746fcda048c3b329e703f14998547578a3b2a6c9e04d551f`
- Source snapshot: `CT-BN-Compat-1.21.1-v2.3.1-source.zip`