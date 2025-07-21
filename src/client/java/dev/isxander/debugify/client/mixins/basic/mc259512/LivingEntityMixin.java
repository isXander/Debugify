package dev.isxander.debugify.client.mixins.basic.mc259512;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-259512", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "eg_fix_horizontal_camera_lag", description = "Increased input delay when riding an entity")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * yHeadRot does not match yRot. yRot is your actual view rotation (the blue line in your debug hitbox),
     * while yHeadRot is interpolated and lags behind (your visible head rotation)
     */
    @ModifyExpressionValue(method = "getViewYRot", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;yHeadRot:F"))
    private float smoothYRot(float original) {
        return (Entity) this instanceof Player ? this.getYRot() : original;
    }

    @ModifyExpressionValue(method = "getViewYRot", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;yHeadRotO:F"))
    private float smoothYRot0(float original) {
        return (Entity) this instanceof Player ? this.yRotO : original;
    }
}
