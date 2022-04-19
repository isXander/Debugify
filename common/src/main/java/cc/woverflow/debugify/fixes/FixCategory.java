package cc.woverflow.debugify.fixes;

public enum FixCategory {
    BASIC("Basic Fixes"),
    GAMEPLAY("Gameplay Fixes");

    private final String displayName;

    FixCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
