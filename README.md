# CT-BN-Compat v2.2.2

Diagnostic-only upstream tracing to determine whether the flaming ruby pickaxe tooltip is
already unstable during ItemStack tooltip generation, or changes inside event dispatch before BAT.
No flicker fix or tooltip/animation behavior change is included.

## What changed

Verified hooks observe ItemStack tooltip entry, the generated list before event construction,
ItemTooltipEvent constructor return, and immediately before/after the real event bus post.
Thirty additional annotated handlers found in the installed mod files have optional read-only
entry/return observations. Their class/method is reported only when actually executing on the
captured event during dispatch. Original listener order is preserved; CT-BN registers no new
tooltip listener and does not wrap or replace the bus.

Component fingerprints now include canonical full component JSON when available, styled glyphs,
type, SHA-256, and bounded serialization excerpts. The diff identifies changed text or styles,
including identical visible text with different serialized properties. Existing JEI, BAT/Icon,
GuiGraphics, ColorTooltips, runtime anchor and descriptor fixes are preserved.

After the first chain, changing frames emit only the earliest divergence and compact diff.
Full chains resume on earliest-owner change, logical-item/hover change, or a five-second heartbeat.
Repeated changed=false outcomes are suppressed even if the handler's incoming text changes.

## Next gameplay test

Close Minecraft and replace v2.2.1 with this JAR, keeping only one CT-BN version loaded.
Open the world and inventory, then hold a stationary JEI image hover on
`betternether:flaming_ruby_pickaxe`. Retain the same-launch debug.log and any crash report.

Search for `TOOLTIP-FIRST-DIVERGENCE firstMutationOwner=`:

- `ITEMSTACK_TOOLTIP_GENERATION`: the list differs before event construction/dispatch.
- A concrete handler's `.AFTER`: first observed changed output at that handler boundary.
- `EVENTBUS_DISPATCH_BEFORE:...`: an unobserved mutation occurred in dispatch before that traced handler.
- `EVENTBUS_DISPATCH_UNTRACED_INTERVAL`: first observed after dispatch returns.

Follow session/frame/logicalItem, dispatch token, and tracedHandlerOrder. Inspect element indexes,
text and representationFirstDifference excerpts for Obsidian Breaker III, Rubys' Fire, and their
descriptions. A relay's output may incorporate delegated listeners; the concrete observed method
is not automatically the ultimate leaf owner. Static candidate discovery does not prove runtime order.

## Build and verification

Use Java 21 and the included Gradle 8.14.3 wrapper. Run --stop, remove this checkout's build
directory, then clean and build --no-build-cache --rerun-tasks. Configuration caching is disabled
to avoid copied checkout paths. Keep reference-dependencies/. Network is required on first build
for real Minecraft/NeoForge dependencies. The check task runs diagnosticTest, including headless
real Minecraft/FML registry fixtures for serialization and ItemStack/event-scope tests.

The original six reference mods remain archived. New optional handlers do not require their
mod binaries to compile: verification records contain actual source JAR hashes and exact methods.
Those optional mod binaries remain in the user's installed instance and are not duplicated in the
source ZIP. An absent or descriptor-incompatible candidate is skipped by the bootstrap gate.

See verification/VERIFICATION.md for final-JAR evidence. Unknown visual payloads and unavailable
serialization are explicitly partial, not proof of stability. No in-game Mixin application or
v2.2.2 flicker reproduction is claimed. The live instance is unchanged.
