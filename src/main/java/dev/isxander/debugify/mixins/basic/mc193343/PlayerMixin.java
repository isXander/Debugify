package dev.isxander.debugify.mixins.basic.mc193343;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-193343", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Player.class)
public class PlayerMixin {
    @ModifyExpressionValue(method = "shouldRemoveSoulSpeed", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z"))
    private boolean shouldRemoveSoulSpeedIfSpectator(boolean flying) {
        return flying || ((Player) (Object) this).isSpectator();
    }
}
