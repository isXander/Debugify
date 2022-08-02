package dev.isxander.debugify;

import dev.isxander.debugify.config.DebugifyConfig;
import dev.isxander.debugify.fixes.BugFix;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Debugify {
    public static Logger logger = LoggerFactory.getLogger("Debugify");
    public static DebugifyConfig config = new DebugifyConfig();
    public static boolean configWasDirty = false;

    /**
     * Called from mixin plugin to manage
     * disabled bug fixes
     */
    public static void onPreInitialize() {
        config.preload();
    }

    public static void onInitialize() {
        configWasDirty = !config.doesJsonHaveIdenticalKeys();
        if (configWasDirty) {
            logger.info("Saving config because the loaded bug fixes are different to stored json.");
            config.save();
        }

        logger.info("Enabled {}/{} bugs!", config.getBugFixes().values().stream().filter((enabled) -> enabled).count(), config.getBugFixes().size());
        logger.info("Successfully Debugify'd your game!");

    }

    public static BugFix.Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? BugFix.Env.CLIENT : BugFix.Env.SERVER;
    }
}
