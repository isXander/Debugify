package dev.isxander.debugify.fixes;

import dev.isxander.debugify.api.DebugifyApi;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public record BugFixData(String bugId, FixCategory category, BugFix.Env env, boolean enabledByDefault, List<String> conflicts, OS requiredOs) implements Comparable<BugFixData> {
    private static final Map<String, Set<String>> externalModConflicts = new HashMap<>();

    public static void registerApiConflict(String modId, String bugId) {
        externalModConflicts.computeIfAbsent(bugId, b -> new HashSet<>()).add(modId);
    }

    public Set<String> getActiveConflicts() {
        Set<String> conflicts = conflicts().stream().filter(id -> FabricLoader.getInstance().isModLoaded(id)).collect(Collectors.toSet());
        Set<String> apiConflicts = externalModConflicts.get(bugId());
        if (apiConflicts != null) conflicts.addAll(apiConflicts);

        return conflicts;
    }

    public boolean satisfiesOSRequirement() {
        return requiredOs() == OS.UNKNOWN || requiredOs() == OS.getOperatingSystem();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BugFixData bugFixData)
            return bugFixData.bugId.equals(bugId);
        return false;
    }

    @Override
    public int compareTo(@NotNull BugFixData o) {
        if (o.bugId.length() == bugId.length()) {
            return bugId.compareTo(o.bugId);
        }

        return Integer.compare(bugId.length(), o.bugId.length());
    }
}
