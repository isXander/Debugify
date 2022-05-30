package dev.isxander.debugify.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ExpectUtilsImpl {
    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
