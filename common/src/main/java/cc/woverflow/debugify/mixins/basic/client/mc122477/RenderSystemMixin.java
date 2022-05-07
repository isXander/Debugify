package cc.woverflow.debugify.mixins.basic.client.mc122477;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import cc.woverflow.debugify.fixes.mc122477.KeyboardPollCounter;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(method = "flipFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwPollEvents()V"))
    private static void onPollEvents(long window, CallbackInfo ci) {
        KeyboardPollCounter.poll();
    }
}
