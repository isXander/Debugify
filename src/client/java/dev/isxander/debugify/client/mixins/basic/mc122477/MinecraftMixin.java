package dev.isxander.debugify.client.mixins.basic.mc122477;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.client.helpers.mc122477.KeyboardPollCounter;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, os = OS.LINUX, description = "Linux/GNU: Opening chat sometimes writes 't'")
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;pollEvents()V"))
    private void onPollEvents(CallbackInfo ci) {
        KeyboardPollCounter.poll();
    }
}
