package dev.isxander.debugify;

import dev.isxander.debugify.config.DebugifyConfig;
import dev.isxander.debugify.fixes.BugFix;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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

        List<String> enabledBugs = config.getBugFixes().entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(entry -> entry.getKey().bugId())
                .toList();
        logger.info("Enabled {} bug fixes: {}", enabledBugs.size(), enabledBugs);
        logger.info("Successfully Debugify'd your game!");
    }

    public static BugFix.Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? BugFix.Env.CLIENT : BugFix.Env.SERVER;
    }
}
