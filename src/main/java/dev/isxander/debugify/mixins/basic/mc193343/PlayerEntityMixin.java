package dev.isxander.debugify.mixins.basic.mc193343;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-193343", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyExpressionValue(method = "shouldRemoveSoulSpeedBoost", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z"))
    private boolean shouldRemoveSoulSpeedIfSpectator(boolean flying) {
        return flying || ((PlayerEntity) (Object) this).isSpectator();
    }
}
