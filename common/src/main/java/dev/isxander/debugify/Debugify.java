package dev.isxander.debugify;

import dev.isxander.debugify.api.updater.UpdateChecker;
import dev.isxander.debugify.config.DebugifyConfig;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.utils.ClientUtils;
import dev.isxander.debugify.utils.Loader;
import com.github.zafarkhaja.semver.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class Debugify {
    public static Logger logger = LoggerFactory.getLogger("Debugify");
    public static Version version = Version.valueOf("@VERSION@");
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

        CompletableFuture.runAsync(() -> {
            if (!config.optOutUpdater) {
                Version latestVersion = UpdateChecker.getLatestVersion();

                if (latestVersion == null)
                    return;

                if (latestVersion.compareTo(version) > 0) {
                    logger.info("An update is available! You're on {} but the latest is {}!", version, latestVersion);
                }
            }
        });

        logger.info("Successfully Debugify'd your game!");
        logger.info("Proudly fixes {} bugs!", config.getBugFixes().values().stream().filter((enabled) -> enabled).count());
    }

    public static boolean isGameplayFixesEnabled() {
        if (Loader.getEnv() == BugFix.Env.CLIENT && ClientUtils.isInMultiplayerWorld())
            return config.gameplayFixesInMultiplayer;

        return true;
    }
}
