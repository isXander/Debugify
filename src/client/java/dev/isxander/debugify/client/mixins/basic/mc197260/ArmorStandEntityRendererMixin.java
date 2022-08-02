package dev.isxander.debugify.client.mixins.basic.mc197260;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.IntStream;

@BugFix(id = "MC-197260", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin extends LivingEntityRendererMixin<ArmorStandEntity, ArmorStandArmorEntityModel> {


    /**
     * Overrides the light level passed to the renderer, with the maximum of:
     * * The block below the armor stand
     * * Bottom of the armor stand
     * * Top of the armor stand
     * * The block above the armor stand
     */
    @Override
    public int modifyProvidedLightLevel(int providedLightLevel, ArmorStandEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
        return Math.max(providedLightLevel, IntStream.of(-1, 2, 3)
                .map(operand -> {
                    BlockPos pos = livingEntity.getBlockPos().add(0, operand, 0);
                    return LightmapTextureManager.pack(livingEntity.world.getLightLevel(LightType.BLOCK, pos), livingEntity.world.getLightLevel(LightType.SKY, pos));
                })
                .max().orElse(providedLightLevel)
        );
    }
}
