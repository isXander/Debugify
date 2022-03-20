package cc.woverflow.debugify;

import cc.woverflow.debugify.api.metrics.UniqueUsersMetric;
import cc.woverflow.debugify.config.DebugifyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Debugify {
    public static Logger logger = LoggerFactory.getLogger("Debugify");

    public static DebugifyConfig config;

    public static void onInitialize() {
        config = new DebugifyConfig();
        config.load();

        if (config.doMetrics) {
            new Thread(UniqueUsersMetric::putApi, "debugify-concurrent").start();
        }

        logger.info("Successfully Debugify'd your game!");
    }
}
