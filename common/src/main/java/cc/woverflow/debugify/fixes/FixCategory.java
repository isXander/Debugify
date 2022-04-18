package cc.woverflow.debugify.fixes;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum FixCategory {
    BASIC(new LiteralText("Basic Fixes")),
    GAMEPLAY(new LiteralText("Gameplay Fixes"));

    private final Text displayName;

    FixCategory(Text displayName) {
        this.displayName = displayName;
    }

    public Text getDisplayName() {
        return this.displayName;
    }
}
