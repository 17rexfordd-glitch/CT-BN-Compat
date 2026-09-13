# CT-BN-Compat v2.2.2 verification

Final JAR SHA-256: `3e5946b816e52d83e1d7cb4b3843917244ce762e111e3993b194a4c882fa829b`
Verified v2.2.1 baseline SHA-256: `84f6203b2108fd227872f33173819a4f848eca8a2ea8cd47596336a4d56ebaee`

## Build and tests

Java 21, Gradle wrapper 8.14.3, Minecraft 1.21.1, NeoForge 21.1.248.
Stopped Gradle, deleted the checked v2.2.2 build directory, ran clean, then
build --no-build-cache --rerun-tasks. No prior v2.2.2 output artifacts existed.
BUILD SUCCESSFUL: all seven project tasks executed. Configuration cache is disabled
to prevent copied checkout paths from being reused. External NeoForm preparation
artifacts were reused; project source and tests were recompiled.

All 40 diagnostic checks passed in the final build. Coverage includes SHA-256,
immutable snapshots and indexed deltas, stable-frame suppression, 500 changing frames
with fewer than 600 records, repeated non-mutating handler suppression, mutation-owner
changes, false-to-true handler mutation detection, full component JSON serialization,
invisible style changes, real ItemStack copy/count/component equality, exact event
correlation, and observed handler order within the captured dispatch. Tests use real
Minecraft/NeoForge APIs with headless registry bootstrap; no simplified API stubs.
Tests exercise diagnostic helpers, not actual in-game Mixin application.

## Exact packaged bytecode

- 65 Java 21 classes; no duplicate entries, dependency/stub classes, or test classes.
- 47 Mixins: all 14 prior Mixins retained, three upstream boundary observers, and
  30 optional concrete subscriber observers. Prior injection selectors retained.
- All 102 exact injection targets and shadow fields verified against real compiled
  dependencies. All 77 inspected Minecraft/NeoForge/JEI method instructions and 12
  field instructions resolve. Interface invocation kinds and Inject.at arrays verified.
- Direct bytecode inspection confirms ItemStack.getTooltipLines invokes the exact
  EventHooks.onItemTooltip method once; EventHooks constructs ItemTooltipEvent before
  its single IEventBus.post(Event):Event call. Packaged BEFORE/AFTER injection targets
  match and straddle that exact interface call.
- EVENT_BUS:IEventBus and slots:NonNullList preserved. Runtime anchor, bootstrap,
  JEI hooks, paired BAT/Icon hooks and metadata checks passed. Version is 2.2.2;
  behavioral-fixes=false.
- No automatic EventBusSubscriber, reflection, runtime references to Mixin classes,
  Redirect/Modify injection, cancellation, or return-value override. Inspected field
  writes target this mod's own diagnostic classes.

## Interpreting runtime evidence

The trace observes witnessed ItemStack generation entry, generated list, event creation,
pre-post, post-post, and concrete handler entry/return. Direct EventHooks calls without
a witnessed matching ItemStack generation are labelled EVENTHOOKS-TOOLTIP-INPUT.
Handler order is only reported for the exact captured event while its post is active.
It is the order of observed handlers, not a complete event-bus listener roster.
Optional subscribers were discovered from exact annotated methods in installed JARs;
their class/method/static descriptors are checked before enabling their Mixins.
Static discovery does not establish runtime order or ownership of a mutation.
Relay handlers may delegate internally. Unobserved dispatch intervals are labelled
as intervals; BAT is never presumed to be the first subscriber.

Logical hover comparison uses copied stack item/components and count. Object identity
only pairs exact event/list/stack scopes, never logical content. Component fingerprints
include runtime class, text, canonical full component JSON and styled glyph data with
SHA-256. Serialization failure and opaque/custom visuals retain explicit partial
coverage. Compact diffs include bounded representation excerpts when styles change.
ColorTooltips/grid hooks without lists explicitly inherit the last sampled fingerprint.

The first full chain is emitted, followed by earliest stage divergence and compact
diffs. Full chains recur on owner/session changes or a roughly five-second heartbeat.
Repeated changed=false pairs are suppressed regardless of changing input while
continuing to sample for a new actual mutation.

No game instance or installed mod was modified or launched. This is a diagnostic-only
release. The next real hover test must determine whether instability already exists
in generated contents or first appears during event dispatch. No flicker fix is claimed.

## Reproduction and evidence

Included: final stop/clean/build logs, bytecode/linkage/boundary audit logs, instruction
references, verifier sources, test source, source diff, and listener-candidate provenance
with audited JAR hashes. Optional candidate mod binaries remain in the user's instance;
string-target Mixins do not require them to compile. Six original reference JARs remain.
Compile VerifyJar, Verify222, VerifyTargets and VerifyUpstreamBoundary using ASM 9.7.1
and asm-tree. Verify222 accepts baseline JAR, new JAR, reference-output path.
VerifyTargets accepts new JAR and newline-separated dependency paths (real built
Minecraft/NeoForge artifacts first, reference JARs/cache dependencies, candidate JARs).
VerifyUpstreamBoundary accepts merged Minecraft JAR, NeoForge JAR, new mod JAR.
The static verifiers do not load game classes.
