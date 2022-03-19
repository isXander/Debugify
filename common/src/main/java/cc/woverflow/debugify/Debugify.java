package cc.woverflow.debugify;

import cc.woverflow.debugify.api.metrics.UniqueUsersMetric;

public class Debugify {
    public static void onInitialize() {
        System.out.println("Hello from Debugify!");
        new Thread(UniqueUsersMetric::putApi, "debugify-concurrent").start();
    }
}
