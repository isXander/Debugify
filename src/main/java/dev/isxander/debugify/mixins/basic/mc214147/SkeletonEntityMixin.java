package dev.isxander.debugify.mixins.basic.mc214147;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.mob.SkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-214147", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(SkeletonEntity.class)
public class SkeletonEntityMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SkeletonEntity;setConversionTime(I)V"))
    private boolean shouldStartConverting(SkeletonEntity instance, int time) {
        return instance.canFreeze();
    }
}
