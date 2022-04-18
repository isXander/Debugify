package cc.woverflow.debugify.fixes;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum FixCategory {
    BASIC("Basic Fixes"),
    GAMEPLAY("Gameplay Fixes");

    private final String displayName;

    FixCategory(String displayName) {
        this.displayName = displayName;
    }

    public Text getDisplayName() {
        return new LiteralText(this.displayName);
    }
}
