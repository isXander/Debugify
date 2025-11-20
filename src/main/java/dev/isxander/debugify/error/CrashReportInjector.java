package dev.isxander.debugify.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

/**
 * Adds details to crash report if Debugify recovered from a mixin application error.
 * This is needed because in some cases, partial mixin application may lead to runtime crashes,
 * and crash reports will not include the original mixin application error details that Debugify
 * logs in {@link DebugifyErrorHandler}.
 */
public class CrashReportInjector {
    public static void addDetailsToCrashReport(StringBuilder sb) {
        Set<MixinErrorEntry> erroredFixes = DebugifyErrorHandler.getErroredFixes();
        if (!erroredFixes.isEmpty()) {
            sb.append("\n-- Debugify recovered from mixin application errors --\n");
            sb.append("Some bug fixes failed to apply, which could have led to this crash.\n");
            sb.append("This log does not mean Debugify caused the crash, but it could have.\n\n");
            for (var entry : erroredFixes) {
                sb.append(stringifyErrorEntry(entry)).append("\n");
            }
        }
    }

    private static String stringifyErrorEntry(MixinErrorEntry entry) {
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        entry.throwable().printStackTrace(printWriter);
        String stacktrace = stringWriter.toString();

        return "%s fix failed to fully apply due to %s during %s\n%s".formatted(
                entry.fix().bugId(),
                entry.mixinInfo().getName(),
                entry.errorStage().name(),
                stacktrace
        );
    }
}
