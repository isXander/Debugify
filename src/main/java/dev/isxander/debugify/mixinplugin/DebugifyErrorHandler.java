package dev.isxander.debugify.mixinplugin;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFixData;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DebugifyErrorHandler implements IMixinErrorHandler {
    private static final Set<BugFixData> ERRORED_FIXES = new HashSet<>();

    @Override
    public ErrorAction onPrepareError(IMixinConfig config, Throwable th, IMixinInfo mixin, ErrorAction action) {
        return handleError(action, mixin);
    }

    @Override
    public ErrorAction onApplyError(String targetClassName, Throwable th, IMixinInfo mixin, ErrorAction action) {
        return handleError(action, mixin);
    }

    private ErrorAction handleError(ErrorAction usualAction, IMixinInfo mixin) {
        ClassNode classNode;
        try {
            classNode = MixinService.getService().getBytecodeProvider().getClassNode(mixin.getClassName(), false);
        } catch (ClassNotFoundException | IOException e) {
            return usualAction;
        }

        Optional<BugFixData> bugFix = MixinPlugin.getBugFixForMixin(classNode);
        if (bugFix.isEmpty())
            return usualAction;

        BugFixData fix = bugFix.get();
        ERRORED_FIXES.add(fix);

        // no need to add exception here, it's already logged under ErrorAction.WARN
        Debugify.LOGGER.error("Failed to fully apply bug fix {}, mixin class {} will not be applied! This may cause runtime errors if a partial injection occurs.", fix.bugId(), mixin.getName());
        return ErrorAction.WARN;
    }

    public static boolean hasErrored(BugFixData fix) {
        return ERRORED_FIXES.contains(fix);
    }
}
