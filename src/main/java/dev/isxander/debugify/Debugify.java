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
    public static final Logger LOGGER = LoggerFactory.getLogger("Debugify");
    public static final DebugifyConfig CONFIG = new DebugifyConfig();
    public static boolean configWasDirty = false;

    /**
     * Called from mixin plugin to manage
     * disabled bug fixes
     */
    public static void onPreInitialize() {
        CONFIG.preload();
    }

    public static void onInitialize() {
        configWasDirty = !CONFIG.doesJsonHaveIdenticalKeys();
        if (configWasDirty) {
            LOGGER.info("Saving config because the loaded bug fixes are different to stored json.");
            CONFIG.save();
        }

        List<String> enabledBugs = CONFIG.getBugFixes().entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(entry -> entry.getKey().bugId())
                .toList();
        LOGGER.info("Enabled {} bug fixes: {}", enabledBugs.size(), enabledBugs);
        LOGGER.info("Successfully Debugify'd your game!");
    }

    public static BugFix.Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? BugFix.Env.CLIENT : BugFix.Env.SERVER;
    }
}
