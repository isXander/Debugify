package cc.woverflow.debugify.mixins.server.mc155509;

import cc.woverflow.debugify.fixes.BugFix;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.PufferfishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-155509", env = BugFix.Env.SERVER)
@Mixin(PufferfishEntity.class)
public class PufferfishEntityMixin {
    @ModifyExpressionValue(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean shouldStingPlayer(boolean damaged) {
        return damaged && ((PufferfishEntity)(Object) this).isAlive();
    }
}
