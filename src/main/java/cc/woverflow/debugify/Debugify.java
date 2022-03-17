package cc.woverflow.debugify;

import cc.woverflow.debugify.api.metrics.UniqueUsersMetric;
import net.fabricmc.api.ModInitializer;

public class Debugify implements ModInitializer {
    @Override
    public void onInitialize() {
        new Thread(UniqueUsersMetric::putApi, "debugify-concurrent").start();
    }
}
