package dev.isxander.debugify.client.mixins.basic.mc118740;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.client.helpers.mc118740.LocalPlayerDuck;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Taken from MoulberrysTweaks
 * https://github.com/Moulberry/MoulberrysTweaks
 * under MIT license
 *
 * @author Moulberry
 */
@BugFix(id = "MC-118740", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "moulberrystweaks", description = "Performing any right-click action silently resets the attack cooldown")
@Mixin(Gui.class)
public class GuiMixin {
    /**
     * This fixes both:
     * MC-118740 - Performing any right-click action silently resets the attack cooldown
     * MC-116510 - Attack indicator doesn't indicate (most of the time) that breaking instantly-mineable blocks resets your attack
     */
    @WrapOperation(
            method = "extractCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"
            )
    )
    public float useFixedCooldown(LocalPlayer instance, float partialTick, Operation<Float> original) {
        if (instance instanceof LocalPlayerDuck localPlayer) {
            return localPlayer.debugify$getVisualAttackStrengthScale(partialTick);
        } else {
            return original.call(instance, partialTick);
        }
    }
}
