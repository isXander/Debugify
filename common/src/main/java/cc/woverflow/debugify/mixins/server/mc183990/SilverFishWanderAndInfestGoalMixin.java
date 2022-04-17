package cc.woverflow.debugify.mixins.server.mc183990;

import cc.woverflow.debugify.fixes.BugFix;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-183990", env = BugFix.Env.SERVER)
@Mixin(SilverfishEntity.WanderAndInfestGoal.class)
public abstract class SilverFishWanderAndInfestGoalMixin extends WanderAroundGoal {
    public SilverFishWanderAndInfestGoalMixin(PathAwareEntity mob, double speed) {
        super(mob, speed);
    }

    @Inject(method = "canStart", at = @At("HEAD"))
    private void removeTargetIfDead(CallbackInfoReturnable<Boolean> cir) {
        if (mob.getTarget() != null && !mob.getTarget().isAlive())
            mob.setTarget(null);
    }
}
