package dev.isxander.debugify.client.mixins.basic.mc118740;

import dev.isxander.debugify.client.helpers.mc118740.LocalPlayerDuck;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Taken from MoulberrysTweaks
 * https://github.com/Moulberry/MoulberrysTweaks
 * under MIT license
 *
 * @author Moulberry
 */
@BugFix(id = "MC-118740", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "moulberrystweaks", description = "Performing any right-click action silently resets the attack cooldown")
@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "resetAttackStrengthTicker", at = @At("HEAD"))
    public void resetAttackStrengthTicker(CallbackInfo ci) {
        if (this instanceof LocalPlayerDuck localPlayerExt) {
            localPlayerExt.debugify$resetVisualAttackStrengthScale();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;moveCloak()V"))
    public void tick(CallbackInfo ci) {
        if (this instanceof LocalPlayerDuck localPlayerExt) {
            localPlayerExt.debugify$incrementVisualAttackStrengthScale();
        }
    }
}
