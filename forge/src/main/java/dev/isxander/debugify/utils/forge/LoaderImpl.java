package dev.isxander.debugify.utils.forge;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.utils.Loader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class LoaderImpl {
    public static Loader getLoader() {
        return Loader.FORGE;
    }

    public static BugFix.Env getEnv() {
        return FMLEnvironment.dist == Dist.CLIENT ? BugFix.Env.CLIENT : BugFix.Env.SERVER;
    }
}
