package dev.isxander.debugify.api;

import java.util.Map;
import java.util.Set;

public interface DebugifyApi {
    /**
     * Returns an array of bug ids (e.g. MC-577) to disable due to this mod
     */
    default String[] getDisabledFixes() {
        return new String[0];
    }

    /**
     * Provides a map of mod id to a set of bug ids to disable fixes
     * due to other mods.
     */
    default Map<String, Set<String>> getProvidedDisabledFixes() {
        return Map.of();
    }
}
