package dev.isxander.debugify.client.mixins.basic.mc22882;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-22882", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, enabled = false)
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @ModifyExpressionValue(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"))
    private boolean hasControlDown(boolean ctrl) {
        if (Util.getOperatingSystem() != Util.OperatingSystem.OSX)
            return ctrl;
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)
                || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_CONTROL);
    }
}
