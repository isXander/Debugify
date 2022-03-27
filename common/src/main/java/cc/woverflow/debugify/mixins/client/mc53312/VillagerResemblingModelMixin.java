package cc.woverflow.debugify.mixins.client.mc53312;

import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Adapted from RandomPatches under MIT License to work in newer versions
 * https://github.com/TheRandomLabs/RandomPatches
 *
 * @author TheRandomLabs
 */
@Mixin(VillagerResemblingModel.class)
public final class VillagerResemblingModelMixin {
    @ModifyConstant(method = "getModelData", constant = @Constant(floatValue = 18.0F))
    private static float getSizeY(float sizeY) {
        return 20.0F;
    }
}
