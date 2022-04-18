package cc.woverflow.debugify.mixins.basic.client.mc53312;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import net.minecraft.client.render.entity.model.ZombieVillagerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@BugFix(id = "MC-53312", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ZombieVillagerEntityModel.class)
public class ZombieVillagerEntityModelMixin {
    @ModifyConstant(method = "getTexturedModelData", constant = @Constant(floatValue = 18.0F))
    private static float getSizeY(float sizeY) {
        return 20.0F;
    }
}
