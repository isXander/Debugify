package cc.woverflow.debugify.utils.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ExpectUtilsImpl {
    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
