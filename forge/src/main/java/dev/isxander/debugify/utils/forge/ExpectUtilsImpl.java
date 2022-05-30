package dev.isxander.debugify.utils.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ExpectUtilsImpl {
    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static boolean isModLoaded(String modId) {
        if (ModList.get() == null) {
            return false;
        }
        return ModList.get().isLoaded(modId);
    }
}
