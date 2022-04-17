package cc.woverflow.debugify.utils.forge;

import cc.woverflow.debugify.Debugify;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

import java.nio.file.Path;
import java.util.List;

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
