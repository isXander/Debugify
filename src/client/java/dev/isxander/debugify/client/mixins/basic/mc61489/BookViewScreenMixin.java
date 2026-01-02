package dev.isxander.debugify.client.mixins.basic.mc61489;

import dev.isxander.debugify.client.utils.ClientUtils;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

// Compared to the original patch, we use / 3 here since it seems to line up better. Else, in a windowed screen the Done button appears at the very bottom / slightly off-screen.
@BugFix(id = "MC-61489", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, enabled = false, modConflicts = {"fixbookgui", "stendhal", "scribble"}, description = "Book GUI is not vertically centered")
@Mixin(BookViewScreen.class)
public class BookViewScreenMixin extends Screen {
    @Shadow
    @Final
    protected static int IMAGE_HEIGHT;

    protected BookViewScreenMixin(Component component) {
        super(component);
    }

    @Overwrite
    private int backgroundTop() {
        return 2 + (this.height - IMAGE_HEIGHT) / 3;
    }
}
