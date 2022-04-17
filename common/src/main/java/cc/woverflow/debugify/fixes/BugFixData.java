package cc.woverflow.debugify.fixes;

public record BugFixData(String bugId, BugFix.Env env, boolean enabledByDefault) {
}
