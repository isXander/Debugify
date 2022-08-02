package dev.isxander.debugify.mixins.basic.mc100991;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-100991", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntityMixin {
    /**
     * notifies the combat tracker
     */
    @Inject(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void onPullEntity(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getDamageTracker().onDamage(DamageSource.thrownProjectile((FishingBobberEntity)(Object)this, getOwner()), livingEntity.getHealth(), 0f);
        }
    }
}
