package dev.isxander.debugify.client.mixins.basic.mc115092;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.world.entity.animal.Squid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-115092", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Squid/glow squid named \"Dinnerbone\" or \"Grumm\" is not upside-down")
@Mixin(SquidRenderer.class)
public class SquidRendererMixin<T extends Squid> {
    @Unique
    T squidEntity;

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/Squid;Lnet/minecraft/client/renderer/entity/state/SquidRenderState;F)V", at = @At("TAIL"))
    private void getSquidEntity(T squid, SquidRenderState squidRenderState, float f, CallbackInfo ci) {
        squidEntity = squid;
    }

    @Inject(method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/SquidRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V", at = @At("TAIL"))
    private void applyRotation(SquidRenderState squidRenderState, PoseStack poseStack, float f, float g, CallbackInfo ci) {
        String name = squidEntity.getName().getString();
        if ("Dinnerbone".equals(name) || "Grumm".equals(name)) {
            poseStack.translate(0.0F, squidEntity.getBbHeight() + 0.1F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        }
    }
}
