package cc.woverflow.debugify.mixins.client.mc53312;

import net.minecraft.client.render.entity.model.ZombieVillagerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ZombieVillagerEntityModel.class)
public class ZombieVillagerEntityModelMixin {
    @ModifyConstant(method = "getTexturedModelData", constant = @Constant(floatValue = 18.0F))
    private static float getSizeY(float sizeY) {
        return 20.0F;
    }
}
