package dev.isxander.debugify.client.mixins.basic.mc93384;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-93384", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Bubbles appear at the feet of drowning mobs")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "makeDrownParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getY()D"))
    private double addEyeHeightToBubblePos(double footY) {
        return footY + getEyeHeight();
    }
}
