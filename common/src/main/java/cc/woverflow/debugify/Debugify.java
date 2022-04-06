package cc.woverflow.debugify;

import cc.woverflow.debugify.api.updater.UpdateChecker;
import cc.woverflow.debugify.config.DebugifyConfig;
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
        configWasDirty = !config.doesJsonMatchConfig();
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
        logger.info("Proudly fixes {} bugs!", config.getBugFixes().size());
    }
}
