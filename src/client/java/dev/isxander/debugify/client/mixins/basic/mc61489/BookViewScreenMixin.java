package dev.isxander.debugify.client.mixins.basic.mc61489;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.debugify.client.utils.ClientUtils;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

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

    @ModifyReturnValue(method = "backgroundTop", at = @At("RETURN"))
    private int modifyTop(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }
}
