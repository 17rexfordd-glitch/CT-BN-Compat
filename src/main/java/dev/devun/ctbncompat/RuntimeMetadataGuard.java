package dev.devun.ctbncompat;

/**
 * Keeps optional runtime metadata verification from blocking the live diagnostic path.
 * The runtime-anchor marker is emitted before metadata verification is attempted.
 */
public final class RuntimeMetadataGuard {
    private RuntimeMetadataGuard() {}

    public static void reportTooltipModsDeferred() {
        DiagnosticLog.once(
                "client-runtime-anchor",
                "CLIENT-RUNTIME-ANCHOR FIRST event=ClientTickEvent.Post access=typed"
        );
        try {
            DiagnosticLog.reportTooltipModsDeferred();
        } catch (Throwable t) {
            DiagnosticLog.once(
                    "optional-runtime-verification-nonfatal-error",
                    "OPTIONAL-RUNTIME-VERIFICATION ERROR nonfatal=true type="
                            + t.getClass().getName()
                            + " message=" + String.valueOf(t.getMessage())
            );
        }
    }
}
