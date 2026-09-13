# CT-BN-Compat v2.3.0

Purpose: clean compatibility release for the proven BetterNether + ImmersiveUI + ColorTooltips tooltip flicker.

## Compatibility hook

v2.3.0 uses one narrow return hook on:

`net.minecraft.world.item.enchantment.Enchantment.getFullname(Holder<Enchantment>, int)`

After ImmersiveUI has had its curse-formatting opportunity, CT-BN checks the enchantment registry ID. Only these two IDs are stabilized:

- `betternether:ruby_fire`
- `betternether:obsidian_breaker`

When ColorTooltips and ImmersiveUI runtime resources are present, CT-BN copies the returned enchantment-name component and recursively normalizes the copy to `ChatFormatting.RED` with `obfuscated=false`. The original tooltip list, item data, enchantment level text, and description lines are not rebuilt or replaced.

## Diagnostic cleanup

Removed the active v2.2.x investigation stack from the final JAR, including JEI chain dumps, ItemStack/enchantment ownership tracing, ImmersiveUI RenderUtils observer tracing, BAT/Icon mutation tracing, large component JSON dumps, and old upstream tracing.

Only concise compatibility logging remains, deduped once per target enchantment:

- `CT-BN-COMPAT stabilized enchantment=betternether:ruby_fire`
- `CT-BN-COMPAT stabilized enchantment=betternether:obsidian_breaker`

## Verification

- Version markers: `2.3.0`.
- `behavioral-fixes=true` is present.
- Final mixin config contains only `EnchantmentNameMixin`.
- No v2.2.x diagnostic classes are packaged: no `EnchantmentTrace`, `TooltipTrace`, `UpstreamTrace`, `ImmersiveEnchantmentStyleMixin`, BAT/Icon/JEI diagnostic mixins, or broad `UpstreamSubscriberXXMixin` classes.
- No stale broken diagnostic calls remain: no `Component.visit(BiFunction, Style)`, no `EnchantmentTags.CURSE:Object`.
- The compatibility code references `Component.copy()`, `MutableComponent.setStyle(Style)`, `Style.withObfuscated(Boolean)`, and `Style.withColor(ChatFormatting.RED)`.
- No tooltip rendering cancellation, tooltip-list replacement, animator reset redirect, or same-item redirect is packaged.
- No bundled Minecraft, NeoForge, Sponge, JEI, ImmersiveUI, Gson, or local stub classes are packaged.
- No duplicate ZIP entries.
- All packaged classes are Java 21 / classfile major 65.

Gradle wrapper could not run in this sandbox because `services.gradle.org` is unreachable; final packaged JAR/classfiles were verified directly.

## Artifact

- JAR: `CT-BN-Compat-1.21.1-v2.3.0.jar`
- SHA-256: `383db879bab1059228d18357be2075730c192074416c7fbb2faa3aca25d08129`
