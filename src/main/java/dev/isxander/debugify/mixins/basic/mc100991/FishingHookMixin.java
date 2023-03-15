package dev.isxander.debugify.mixins.basic.mc100991;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-100991", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends ProjectileMixin {
    public FishingHookMixin(EntityType<?> variant, Level world) {
        super(variant, world);
    }

    /**
     * notifies the combat tracker
     */
    @Inject(method = "pullEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void onPullEntity(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getCombatTracker().recordDamage(
                    level.damageSources().thrown((FishingHook)(Object)this, getOwner()),
                    livingEntity.getHealth(),
                    0f
            );
        }
    }
}
