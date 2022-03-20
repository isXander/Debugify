package cc.woverflow.debugify.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ExpectUtilsImpl {
    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
