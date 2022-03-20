package cc.woverflow.debugify;

import cc.woverflow.debugify.api.updater.UpdateChecker;
import com.github.zafarkhaja.semver.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Debugify {
    public static Logger logger = LoggerFactory.getLogger("Debugify");
    public static Version version = Version.valueOf("@VERSION@");

    public static void onInitialize() {
        new Thread(() -> {
            Version latestVersion = UpdateChecker.getLatestVersion();

            if (latestVersion == null)
                return;

            if (latestVersion.compareTo(version) > 0) {
                logger.info("An update is available! You're on {} but the latest is {}!", version, latestVersion);
            }
        }, "debugify-concurrent").start();

        logger.info("Successfully Debugify'd your game!");
    }
}
