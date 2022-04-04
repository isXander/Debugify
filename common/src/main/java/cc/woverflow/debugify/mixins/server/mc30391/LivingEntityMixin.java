package cc.woverflow.debugify.mixins.server.mc30391;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyExpressionValue(method = "fall", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z", ordinal = 1))
    private boolean shouldntSpawnParticles(boolean isClient) {
        LivingEntity self = (LivingEntity) (Object) this;
        return isClient || self instanceof ChickenEntity || self instanceof BlazeEntity || self instanceof WitherEntity;
    }
}
