package cc.woverflow.debugify.utils.fabric;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.utils.Loader;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class LoaderImpl {
    public static Loader getLoader() {
        return Loader.FABRIC;
    }

    public static BugFix.Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? BugFix.Env.CLIENT : BugFix.Env.SERVER;
    }
}
