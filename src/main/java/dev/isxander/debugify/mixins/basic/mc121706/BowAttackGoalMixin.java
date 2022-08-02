package dev.isxander.debugify.mixins.basic.mc121706;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-121706", env = BugFix.Env.SERVER, category = FixCategory.BASIC)
@Mixin(BowAttackGoal.class)
public abstract class BowAttackGoalMixin<T extends HostileEntity> {
    @Shadow @Final private T actor;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/HostileEntity;lookAtEntity(Lnet/minecraft/entity/Entity;FF)V", shift = At.Shift.AFTER))
    private void lookAtTarget(CallbackInfo ci) {
        actor.getLookControl().lookAt(actor.getTarget(), 30f, 30f);
    }
}
