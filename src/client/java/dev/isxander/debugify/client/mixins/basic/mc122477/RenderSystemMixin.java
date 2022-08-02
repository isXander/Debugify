package dev.isxander.debugify.client.mixins.basic.mc122477;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.client.fixes.mc122477.KeyboardPollCounter;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(value = RenderSystem.class, priority = 1100) // higher priority to improve compatibility with VulkanMod
public class RenderSystemMixin {
    @Inject(method = "flipFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwPollEvents()V"))
    private static void onPollEvents(long window, CallbackInfo ci) {
        KeyboardPollCounter.poll();
    }
}
