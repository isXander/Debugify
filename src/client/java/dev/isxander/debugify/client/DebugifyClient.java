package dev.isxander.debugify.client;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.utils.BugFixDescriptionCache;

public class DebugifyClient {
    public static BugFixDescriptionCache bugFixDescriptionCache;

    public static void onInitializeClient() {
        bugFixDescriptionCache = new BugFixDescriptionCache();

        if (Debugify.configWasDirty) {
            Debugify.LOGGER.info("Re-caching descriptions because json doesn't match config.");
            bugFixDescriptionCache.cacheDescriptions();
        } else {
            if (!bugFixDescriptionCache.load()) {
                Debugify.LOGGER.info("Failed to load descriptions, re-caching.");
                bugFixDescriptionCache.cacheDescriptions();
            }
        }
    }

    public static boolean isGameplayFixesEnabled() {
        return Debugify.CONFIG.gameplayFixesInMultiplayer;
    }
}
