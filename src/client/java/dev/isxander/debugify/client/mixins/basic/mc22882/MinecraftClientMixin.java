package dev.isxander.debugify.client.mixins.basic.mc22882;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-22882", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, os = OS.MAC)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Final public static boolean IS_SYSTEM_MAC;

    @ModifyArg(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;dropSelectedItem(Z)Z"))
    private boolean shouldDropEntireStack(boolean ctrlPressed) {
        if (!IS_SYSTEM_MAC)
            return ctrlPressed;
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)
                || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_CONTROL);
    }
}
