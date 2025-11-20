package dev.isxander.debugify;

import dev.isxander.debugify.config.DebugifyConfig;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.error.DebugifyErrorHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;
import java.util.Map;

public class Debugify {
    public static final Logger LOGGER = LoggerFactory.getLogger("Debugify");
    public static final Version VERSION = FabricLoader.getInstance().getModContainer("debugify").orElseThrow().getMetadata().getVersion();
    public static final DebugifyConfig CONFIG = new DebugifyConfig();

    /**
     * Called from mixin plugin to manage
     * disabled bug fixes
     */
    public static void onPreInitialize() {
        CONFIG.preload();
        Mixins.registerErrorHandlerClass(DebugifyErrorHandler.class.getName());
    }

    public static void onInitialize() {
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

    public static boolean isGameplayFixesEnabled() {
        return Debugify.CONFIG.gameplayFixesInMultiplayer;
    }
}
