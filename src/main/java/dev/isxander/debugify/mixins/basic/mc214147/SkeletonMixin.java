package dev.isxander.debugify.mixins.basic.mc214147;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.monster.Skeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-214147", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Skeleton.class)
public class SkeletonMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Skeleton;startFreezeConversion(I)V"))
    private boolean shouldStartConverting(Skeleton instance, int time) {
        return instance.canFreeze();
    }
}
