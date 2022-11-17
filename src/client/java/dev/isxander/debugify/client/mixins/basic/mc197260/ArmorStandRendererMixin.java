package dev.isxander.debugify.client.mixins.basic.mc197260;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.IntStream;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.LightLayer;

@BugFix(id = "MC-197260", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRendererMixin<ArmorStand, ArmorStandArmorModel> {

    /**
     * Overrides the light level passed to the renderer, with the maximum of:
     * <ul>
     *     <li>The block below the armor stand</li>
     *     <li>Bottom of the armor stand</li>
     *     <li>Top of the armor stand</li>
     *     <li>The block above the armor stand</li>
     * </ul>
     */
    @Override
    public int modifyLightLevel(int providedLightLevel, ArmorStand livingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider) {
        return Math.max(providedLightLevel, IntStream.of(-1, 2, 3)
                .map(operand -> {
                    BlockPos pos = livingEntity.blockPosition().offset(0, operand, 0);
                    return LightTexture.pack(livingEntity.level.getBrightness(LightLayer.BLOCK, pos), livingEntity.level.getBrightness(LightLayer.SKY, pos));
                })
                .max().orElse(providedLightLevel)
        );
    }
}
