package dev.isxander.debugify.mixins.basic.mc183990;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-183990", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(targets = "net.minecraft.world.entity.monster.Silverfish$SilverfishMergeWithStoneGoal")
public abstract class SilverfishMergeWithStoneGoalMixin extends RandomStrollGoal {
    public SilverfishMergeWithStoneGoalMixin(PathfinderMob mob, double speed) {
        super(mob, speed);
    }

    @Inject(method = "canUse", at = @At(value = "RETURN", ordinal = 0))
    private void removeTargetIfDead(CallbackInfoReturnable<Boolean> cir) {
        if (!mob.getTarget().isAlive())
            mob.setTarget(null);
    }
}
