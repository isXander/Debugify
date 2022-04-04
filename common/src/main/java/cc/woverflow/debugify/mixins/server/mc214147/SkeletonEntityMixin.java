package cc.woverflow.debugify.mixins.server.mc214147;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.mob.SkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkeletonEntity.class)
public class SkeletonEntityMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SkeletonEntity;setConversionTime(I)V"))
    private boolean shouldStartConverting(SkeletonEntity instance, int time) {
        return instance.canFreeze();
    }
}
