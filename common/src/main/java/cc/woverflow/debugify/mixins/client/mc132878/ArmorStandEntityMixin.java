package cc.woverflow.debugify.mixins.client.mc132878;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin {
    @Shadow protected abstract void spawnBreakParticles();

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;onBreak(Lnet/minecraft/entity/damage/DamageSource;)V"))
    private void breakParticlesExplosion(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.spawnBreakParticles();
    }

    @Inject(method = "updateHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;onBreak(Lnet/minecraft/entity/damage/DamageSource;)V"))
    private void breakParticlesFire(DamageSource damageSource, float amount, CallbackInfo ci) {
        this.spawnBreakParticles();
    }
}
