package dev.isxander.debugify.fixes;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record BugFixData(String bugId, FixCategory category, BugFix.Env env, boolean enabledByDefault, List<String> conflicts, OS requiredOs) implements Comparable<BugFixData> {
    public List<String> getActiveConflicts() {
        return conflicts().stream().filter(id -> FabricLoader.getInstance().isModLoaded(id)).toList();
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
