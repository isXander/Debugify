package dev.isxander.debugify.mixins.basic.mc30391;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-30391", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyExpressionValue(method = "checkFallDamage", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 1))
    private boolean shouldntSpawnParticles(boolean isClient) {
        LivingEntity self = (LivingEntity) (Object) this;
        return isClient || self instanceof Chicken || self instanceof Blaze || self instanceof WitherBoss;
    }
}
