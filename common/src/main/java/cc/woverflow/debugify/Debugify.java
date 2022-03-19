package cc.woverflow.debugify;

import cc.woverflow.debugify.api.metrics.UniqueUsersMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Debugify {
    public static Logger logger = LoggerFactory.getLogger("Debugify");

    public static void onInitialize() {
        new Thread(UniqueUsersMetric::putApi, "debugify-concurrent").start();
        logger.info("Successfully Debugify'd your game!");
    }
}
