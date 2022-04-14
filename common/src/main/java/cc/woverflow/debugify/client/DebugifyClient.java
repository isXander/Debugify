package cc.woverflow.debugify.client;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.client.utils.BugFixDescriptionCache;

public class DebugifyClient {
    public static BugFixDescriptionCache bugFixDescriptionCache;

    public static void onInitializeClient() {
        bugFixDescriptionCache = new BugFixDescriptionCache();

        if (Debugify.configWasDirty) {
            Debugify.logger.info("Re-caching descriptions because json doesn't match config.");
            bugFixDescriptionCache.trySave();
        } else {
            if (!bugFixDescriptionCache.load()) {
                Debugify.logger.info("Failed to load descriptions, re-caching.");
                bugFixDescriptionCache.trySave();
            }
        }
    }
}
