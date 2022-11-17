package dev.isxander.debugify.mixins.basic.mc72151;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@BugFix(id = "MC-72151", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Wolf.class)
public class WolfMixin {
    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/TamableAnimal;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.BEFORE), ordinal = 0, argsOnly = true)
    private float modifyAmount(float amount, DamageSource source) {
        Entity entity = source.getEntity();

        if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
            // wolves modify damage = (amount + 1) / 2
            // so (0 + 1) / 2 = 0.5
            if (amount == 0.5)
                return 0;
        }

        // else just turn the original
        return amount;
    }
}
