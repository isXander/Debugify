package dev.isxander.debugify.client.mixins.basic.mc280220;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.DolphinCarryingItemLayer;
import net.minecraft.client.renderer.entity.state.DolphinRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-280220", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "When a Dolphin holds an item, it is rendered upside-down")
@Mixin(DolphinCarryingItemLayer.class)
public class DolphinCarryingItemLayerMixin {
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/DolphinRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"))
    private void flipItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, DolphinRenderState dolphinRenderState, float f, float g, CallbackInfo ci) {
        poseStack.mulPose(Axis.ZP.rotationDegrees(180F));
        poseStack.translate(0F, -0.4F, 0F); // Eyeballed value to make items not float
    }
}
