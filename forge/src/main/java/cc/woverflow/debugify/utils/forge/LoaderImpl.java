package cc.woverflow.debugify.utils.forge;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.utils.Loader;
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
