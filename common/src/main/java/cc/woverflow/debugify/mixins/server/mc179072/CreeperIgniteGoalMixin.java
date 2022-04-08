package cc.woverflow.debugify.mixins.server.mc179072;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperIgniteGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreeperIgniteGoal.class)
public class CreeperIgniteGoalMixin {
    @Shadow private @Nullable LivingEntity target;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobVisibilityCache;canSee(Lnet/minecraft/entity/Entity;)Z"))
    private boolean shouldIgniteCreeper(boolean canSeeTarget) {
        return canSeeTarget && target.canTakeDamage();
    }
}
