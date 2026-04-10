package dev.isxander.debugify.mixins.basic.mc264979;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.dedicated.Settings;
import org.spongepowered.asm.mixin.Mixin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@BugFix(id = "MC-264979", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Fresh installations print NoSuchFileException when attempting to load server.properties")
@Mixin(Settings.class)
public class SettingsMixin {
    @WrapMethod(method = "loadFromFile")
    private static Properties dontLoadMissingFile(Path file, Operation<Properties> original) {
        if (Files.notExists(file)) {
            return new Properties();
        }
        return original.call(file);
    }
}
