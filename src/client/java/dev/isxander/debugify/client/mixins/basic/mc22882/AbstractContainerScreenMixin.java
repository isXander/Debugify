package dev.isxander.debugify.client.mixins.basic.mc22882;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.InputConstants;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-22882", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, os = OS.MAC, modConflicts = "ctrl-q", description = "Ctrl + Q won't work on Mac")
@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @ModifyExpressionValue(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/KeyEvent;hasControlDown()Z"))
    private boolean hasControlDown(boolean ctrl) {
        var window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window, InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(window, InputConstants.KEY_RCONTROL);
    }
}
