package dev.isxander.debugify.mixins.basic.mc183990;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-183990", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Mob.class)
public abstract class MobMixin {
    @Shadow private @Nullable LivingEntity target;

    @Shadow public abstract void setTarget(@Nullable LivingEntity target);

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.BEFORE))
    private void clearTargetIfDead(CallbackInfo ci) {
        if (target != null && target.isDeadOrDying()) {
            setTarget(null);
        }
    }
}
