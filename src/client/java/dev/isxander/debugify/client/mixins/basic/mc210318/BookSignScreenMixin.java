package dev.isxander.debugify.client.mixins.basic.mc210318;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.BookSignScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-210318", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Maximum length of book title changed from 16 to 15 characters")
@Mixin(BookSignScreen.class)
public class BookSignScreenMixin {
    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setMaxLength(I)V"))
    private void increaseBookTitleMaxLength(EditBox instance, int i, Operation<Void> original) {
        original.call(instance, 16);
    }
}
