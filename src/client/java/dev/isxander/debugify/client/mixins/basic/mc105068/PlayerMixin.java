package dev.isxander.debugify.client.mixins.basic.mc105068;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@BugFix(id = "MC-105068", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(Player.class)
public class PlayerMixin {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean onAttack(Entity target, DamageSource source, float amount) {
        if (target instanceof LivingEntity e && e.isDamageSourceBlocked(source)) return false;
        return target.hurt(source, amount);
    }
}
