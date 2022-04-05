package cc.woverflow.debugify.client;

import cc.woverflow.debugify.client.utils.BugFixDescriptionHolder;

public class DebugifyClient {
    public static void onInitializeClient() {
        BugFixDescriptionHolder.cacheDescriptions();
    }
}
