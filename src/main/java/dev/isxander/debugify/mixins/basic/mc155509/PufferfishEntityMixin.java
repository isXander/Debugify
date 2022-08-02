package dev.isxander.debugify.mixins.basic.mc155509;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.passive.PufferfishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-155509", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(PufferfishEntity.class)
public class PufferfishEntityMixin {
    @ModifyExpressionValue(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean shouldStingPlayer(boolean damaged) {
        return damaged && ((PufferfishEntity)(Object) this).isAlive();
    }
}
