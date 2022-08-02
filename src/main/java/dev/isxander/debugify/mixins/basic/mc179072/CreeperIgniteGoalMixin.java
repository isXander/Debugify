package dev.isxander.debugify.mixins.basic.mc179072;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperIgniteGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-179072", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(CreeperIgniteGoal.class)
public class CreeperIgniteGoalMixin {
    @Shadow private @Nullable LivingEntity target;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobVisibilityCache;canSee(Lnet/minecraft/entity/Entity;)Z"))
    private boolean shouldIgniteCreeper(boolean canSeeTarget) {
        return canSeeTarget && target.canTakeDamage();
    }
}
