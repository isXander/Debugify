package dev.isxander.debugify.mixins.basic.client.mc148149;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

@BugFix(id = "MC-148149", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = "fastopenlinksandfolders")
@Mixin(Util.OperatingSystem.class)
public abstract class OperatingSystemMixin {
    @Shadow protected abstract String[] getURLOpenCommand(URL url);

    /**
     * @author isXander
     */
    @Overwrite
    public void open(URL url) {
        try {
            Debugify.logger.info("Opening non-blocking URL. You may see system errors in the logs you can ignore.");
            AccessController.doPrivileged((PrivilegedExceptionAction<Process>)() -> new ProcessBuilder().command(this.getURLOpenCommand(url)).redirectError(ProcessBuilder.Redirect.INHERIT).start());
        } catch (PrivilegedActionException e) {
            UtilAccessor.getLogger().error("Couldn't open url '{}'", url, e);
        }
    }
}
