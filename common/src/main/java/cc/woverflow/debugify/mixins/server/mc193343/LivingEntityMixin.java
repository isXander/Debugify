package cc.woverflow.debugify.mixins.server.mc193343;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyExpressionValue(method = "shouldRemoveSoulSpeedBoost", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
    private boolean shouldRemoveSoulSpeedIfSpectator(boolean elytraFlying) {
        boolean spectator = false;
        if (((LivingEntity) (Object) this) instanceof PlayerEntity player) {
            spectator = player.isSpectator();
        }

        return elytraFlying || spectator;
    }
}
