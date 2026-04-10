package dev.isxander.debugify.client.mixins.basic.mc105068;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-105068", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Attacking a shield-blocking player always plays the player hurts sound")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> variant, Level world) {
        super(variant, world);
    }

    @WrapOperation(
            method = "handleEntityEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            )
    )
    private void beforeCond30(LivingEntity instance, SoundEvent soundEvent, float volume, float pitch, Operation<Void> original) {
        level().playLocalSound(getX(), getY(), getZ(), soundEvent, getSoundSource(), volume, pitch, false);
    }
}
