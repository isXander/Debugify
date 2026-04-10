package dev.isxander.debugify.mixins.basic.mc268617;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.util.FileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-268617", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Structures can't be saved if the game directory is named in a certain way")
@Mixin(FileUtil.class)
public class FileUtilMixin {
    /**
     * Updates the valid path RegEx to include more valid file paths. This new filter will properly match all reserved file names, including many undocumented by Microsoft.
     * This fixes structure blocks for normal installations on old Modrinth instances or certain Linux scenarios such as Flatpak installations.
     * Can be tested on Windows by naming an instance "com.example".
     */
    @Definition(id = "RESERVED_WINDOWS_FILENAMES", field = "Lnet/minecraft/util/FileUtil;RESERVED_WINDOWS_FILENAMES:Ljava/util/regex/Pattern;")
    @Definition(id = "compile", method = "Ljava/util/regex/Pattern;compile(Ljava/lang/String;I)Ljava/util/regex/Pattern;")
    @Expression("RESERVED_WINDOWS_FILENAMES = @(compile(?, ?))")
    @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static String fixWindowsReservedFilename(String regex) {
        return ".*\\.|(?:CON|PRN|AUX|NUL|CLOCK\\$|CONIN\\$|CONOUT\\$|(?:COM|LPT)[¹²³0-9])(?:\\..*)?";
    }
}
