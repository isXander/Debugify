package dev.isxander.debugify.client.mixins.basic.mc233042;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-233042", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(DirectConnectScreen.class)
public class DirectConnectScreenMixin {
    /**
     * Set setTextFieldFocused to false as it seems to fix the issue
     */
    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setTextFieldFocused(Z)V"))
    private boolean shouldFocusTextField(boolean focused) {
        return false;
    }
}
