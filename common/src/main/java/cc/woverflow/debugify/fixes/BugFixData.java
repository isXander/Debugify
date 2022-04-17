package cc.woverflow.debugify.fixes;

import cc.woverflow.debugify.utils.ExpectUtils;

import java.util.ArrayList;
import java.util.List;

public record BugFixData(String bugId, BugFix.Env env, boolean enabledByDefault, List<String> conflicts) {
    public List<String> getActiveConflicts() {
        List<String> activeConflicts = new ArrayList<>();
        for (String conflict : conflicts)
            if (ExpectUtils.isModLoaded(conflict))
                activeConflicts.add(conflict);
        return activeConflicts;
    }
}
