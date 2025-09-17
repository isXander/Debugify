package dev.isxander.debugify.client.mixins.basic.mc61489;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Compared to the original patch, we use / 3 here since it seems to line up better. Else, in a windowed screen the Done button appears at the very bottom / slightly off-screen.
@BugFix(id = "MC-61489", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = {"fixbookgui", "scribble"}, description = "Book GUI is not vertically centered")
@Mixin(LecternScreen.class)
public class LecternScreenMixin extends BookViewScreen {
    @ModifyArg(method = "createMenuControls", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"), index = 1)
    private int modifyMenuControlsYPos(int original) {
        return original + (this.height - BookViewScreen.IMAGE_HEIGHT) / 3;
    }
}
