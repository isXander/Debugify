package dev.isxander.debugify.client.mixins.basic.mc197260;

import com.mojang.datafixers.util.Pair;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Comparator;
import java.util.stream.IntStream;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.LightLayer;

@BugFix(id = "MC-197260", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Armor Stand renders itself and armor dark if its head is in a solid block")
@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRendererMixin<ArmorStand, ArmorStandRenderState, ArmorStandArmorModel> {
    protected ArmorStandRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

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
    public void debugify$modifyLightCoords(ArmorStandRenderState livingEntity) {
        BlockPos mainPos = BlockPos.containing(livingEntity.x, livingEntity.y, livingEntity.z);
        ClientLevel level = Minecraft.getInstance().level;

        livingEntity.lightCoords = IntStream.of(-1, 0, 2, 3)
                .mapToObj(operand -> {
                    BlockPos pos = mainPos.offset(0, operand, 0);
                    return Pair.of(level.getBrightness(LightLayer.BLOCK, pos), pos);
                })
                .max(Comparator.comparingInt(Pair::getFirst))
                .map(p -> LightTexture.pack(p.getFirst(), level.getBrightness(LightLayer.SKY, p.getSecond())))
                .orElse(livingEntity.lightCoords);
    }
}
