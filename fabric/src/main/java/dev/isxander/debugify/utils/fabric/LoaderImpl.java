package dev.isxander.debugify.utils.fabric;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.utils.Loader;
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
