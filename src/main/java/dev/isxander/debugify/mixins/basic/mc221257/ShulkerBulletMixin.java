package dev.isxander.debugify.mixins.basic.mc221257;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-221257", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Shulker bullets don't produce bubble particles when moving through water")
@Mixin(ShulkerBullet.class)
public abstract class ShulkerBulletMixin extends Projectile {
    public ShulkerBulletMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Code taken from {@link net.minecraft.world.entity.projectile.arrow.AbstractArrow}
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ShulkerBullet;setPos(Lnet/minecraft/world/phys/Vec3;)V"))
    private void addMissingBubbleParticles(CallbackInfo ci) {
        if (this.isInWater()) {
            Vec3 vec3 = this.position();
            Vec3 vec32 = this.getDeltaMovement();

            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, vec3.x - vec32.x * (double) 0.25F, vec3.y - vec32.y * (double) 0.25F, vec3.z - vec32.z * (double) 0.25F, vec32.x, vec32.y, vec32.z);
            }
        }
    }
}
