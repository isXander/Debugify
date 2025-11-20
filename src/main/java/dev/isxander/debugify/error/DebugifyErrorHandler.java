package dev.isxander.debugify.error;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFixData;
import dev.isxander.debugify.mixinplugin.BugFixDataCache;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DebugifyErrorHandler implements IMixinErrorHandler {
    private static final Set<MixinErrorEntry> ERRORED_FIXES = new HashSet<>();

    @Override
    public ErrorAction onPrepareError(IMixinConfig config, Throwable th, IMixinInfo mixin, ErrorAction action) {
        return handleError(action, mixin, th, ErrorStage.PREPARE);
    }

    @Override
    public ErrorAction onApplyError(String targetClassName, Throwable th, IMixinInfo mixin, ErrorAction action) {
        return handleError(action, mixin, th, ErrorStage.APPLY);
    }

    private ErrorAction handleError(ErrorAction usualAction, IMixinInfo mixin, Throwable th, ErrorStage stage) {
        Optional<BugFixData> bugFix = BugFixDataCache.getIfResolved(mixin.getClassName());
        if (bugFix.isEmpty())
            return usualAction;

        BugFixData fix = bugFix.get();
        ERRORED_FIXES.add(new MixinErrorEntry(fix, mixin, th, stage));

        // no need to add exception here, it's already logged under ErrorAction.WARN
        Debugify.LOGGER.error("Failed to fully apply bug fix {}, mixin class {} will not be applied! This may cause runtime errors if a partial injection occurs.", fix.bugId(), mixin.getName());
        return ErrorAction.WARN;
    }

    public static boolean hasErrored(BugFixData fix) {
        return ERRORED_FIXES.stream().anyMatch(entry -> entry.fix().equals(fix));
    }

    public static Set<MixinErrorEntry> getErroredFixes() {
        return Collections.unmodifiableSet(ERRORED_FIXES);
    }

}
