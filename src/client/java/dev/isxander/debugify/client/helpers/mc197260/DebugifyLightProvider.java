package dev.isxander.debugify.client.helpers.mc197260;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;

public interface DebugifyLightProvider<T extends LivingEntity> {
    default int modifyLightLevel(int lightLevel, T livingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider) {
        return lightLevel;
    }
}
