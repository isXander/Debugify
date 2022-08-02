package dev.isxander.debugify.client.fixes.mc197260;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public interface DebugifyLightProvider<T extends LivingEntity> {
    default int modifyProvidedLightLevel(int lightLevel, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
        return lightLevel;
    }
}
