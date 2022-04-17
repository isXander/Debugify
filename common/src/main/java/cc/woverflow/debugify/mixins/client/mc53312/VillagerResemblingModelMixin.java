package cc.woverflow.debugify.mixins.client.mc53312;

import cc.woverflow.debugify.fixes.BugFix;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Taken from <a href="https://github.com/TheRandomLabs/RandomPatches">RandomPatches</a>
 * under MIT license
 *
 * Adapted to work in newer versions and a multi-loader environment
 *
 * @author TheRandomLabs
 */
@BugFix(id = "MC-53312", env = BugFix.Env.CLIENT)
@Mixin(VillagerResemblingModel.class)
public final class VillagerResemblingModelMixin {
    @ModifyConstant(method = "getModelData", constant = @Constant(floatValue = 18.0F))
    private static float getSizeY(float sizeY) {
        return 20.0F;
    }
}
