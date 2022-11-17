package dev.isxander.debugify.mixins.basic.mc179072;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-179072", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(SwellGoal.class)
public class SwellMixin {
    @Shadow private @Nullable LivingEntity target;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/Sensing;hasLineOfSight(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean shouldIgniteCreeper(boolean canSeeTarget) {
        return canSeeTarget && target.canBeSeenAsEnemy();
    }
}
