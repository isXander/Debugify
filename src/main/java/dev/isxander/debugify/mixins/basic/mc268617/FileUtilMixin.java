package dev.isxander.debugify.mixins.basic.mc268617;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.FileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.regex.Pattern;

@BugFix(id = "MC-268617", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Structures can't be saved if the game directory is named in a certain way")
@Mixin(FileUtil.class)
public class FileUtilMixin {
    /**
     * Updates the valid path RegEx to include more valid file paths. This new filter will properly match all reserved file names, including many undocumented by Microsoft.
     * This fixes structure blocks for normal installations on old Modrinth instances or certain Linux scenarios such as Flatpak installations.
     * Can be tested on Windows by naming an instance "com.example".
     */
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/regex/Pattern;compile(Ljava/lang/String;I)Ljava/util/regex/Pattern;", ordinal = 1))
    private static Pattern fixWindowsReservedFilename(String regex, int flags, Operation<Pattern> original) {
        return original.call(".*\\.|(?:CON|PRN|AUX|NUL|CLOCK\\$|CONIN\\$|CONOUT\\$|(?:COM|LPT)[¹²³0-9])(?:\\..*)?", flags);
    }
}
