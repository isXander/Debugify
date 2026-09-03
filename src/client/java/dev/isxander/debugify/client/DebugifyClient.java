package dev.isxander.debugify.client;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.utils.BugFixDescriptionCache;
import dev.isxander.debugify.client.utils.ClientUtils;

public class DebugifyClient {
    public static BugFixDescriptionCache bugFixDescriptionCache;

    public static void onInitializeClient() {
        bugFixDescriptionCache = new BugFixDescriptionCache();
        bugFixDescriptionCache.loadDescriptions();
        bugFixDescriptionCache.cacheMissingDescriptions();
    }

    public static boolean isGameplayFixesEnabled() {
        return !ClientUtils.isInMultiplayerWorld() || Debugify.CONFIG.gameplayFixesInMultiplayer;
    }
}
