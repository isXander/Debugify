package dev.isxander.debugify.client.mixins.basic.mc46737;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-46737", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Entities' shaders are applied when beginning to spectate them in third person")
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @WrapWithCondition(method = "checkEntityPostEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;setPostEffect(Lnet/minecraft/resources/ResourceLocation;)V"))
    private boolean thirdPersonCheck(GameRenderer renderer, ResourceLocation location) {
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }
}
