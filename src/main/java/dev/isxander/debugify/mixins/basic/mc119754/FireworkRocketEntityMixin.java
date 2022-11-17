package dev.isxander.debugify.mixins.basic.mc119754;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-119754", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    @Shadow @Nullable
    private LivingEntity attachedToEntity;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isFallFlying()Z"))
    private boolean shouldFly(boolean elytraFlying) {
        return elytraFlying && !attachedToEntity.isSpectator();
    }
}
