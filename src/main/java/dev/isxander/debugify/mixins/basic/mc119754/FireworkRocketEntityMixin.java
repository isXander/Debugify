package dev.isxander.debugify.mixins.basic.mc119754;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-119754", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    @Shadow @Nullable
    private LivingEntity shooter;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
    private boolean shouldFly(boolean elytraFlying) {
        return elytraFlying && !shooter.isSpectator();
    }
}
