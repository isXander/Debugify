package dev.isxander.debugify.mixins.basic.mc227337;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-227337", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "When a shulker bullet hits an entity, the explodes sound is not played and particles are not produced")
@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {
    @Inject(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/EntityHitResult;getEntity()Lnet/minecraft/world/entity/Entity;"
            )
    )
    private void playSoundAndDisplayParticles(EntityHitResult hitResult, CallbackInfo ci) {
        ShulkerBullet instance = (ShulkerBullet) (Object) this;
        ((ServerLevel) instance.level()).sendParticles(ParticleTypes.EXPLOSION, instance.getX(), instance.getY(), instance.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
        instance.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
    }
}
