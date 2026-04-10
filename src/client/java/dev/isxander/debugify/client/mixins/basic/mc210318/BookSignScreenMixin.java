package dev.isxander.debugify.client.mixins.basic.mc210318;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.inventory.BookSignScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-210318", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Maximum length of book title changed from 16 to 15 characters")
@Mixin(BookSignScreen.class)
public class BookSignScreenMixin {

    @ModifyArg(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/EditBox;setMaxLength(I)V"
            )
    )
    private int restoreBookTitleMaxLength(int length) {
        return 16;
    }
}
