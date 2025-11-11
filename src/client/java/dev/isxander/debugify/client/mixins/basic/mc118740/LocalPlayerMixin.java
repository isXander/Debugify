package dev.isxander.debugify.client.mixins.basic.mc118740;

import com.mojang.authlib.GameProfile;
import dev.isxander.debugify.client.helpers.mc118740.LocalPlayerDuck;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Taken from <a href="https://github.com/Moulberry/MoulberrysTweaks">MoulberrysTweaks</a>
 * under MIT license
 *
 * @author Moulberry
 */
@BugFix(id = "MC-118740", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "moulberrystweaks", description = "Performing any right-click action silently resets the attack cooldown")
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player implements LocalPlayerDuck {
    @Unique
    private int visualAttackStrengthTicker = 0;

    public LocalPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "swing", at = @At("HEAD"))
    public void swing(InteractionHand interactionHand, CallbackInfo ci) {
        this.visualAttackStrengthTicker = 0;
    }

    @Override
    public float debugify$getVisualAttackStrengthScale(float partialTick) {
        return Mth.clamp(((float)this.visualAttackStrengthTicker + partialTick) / this.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    @Override
    public void debugify$resetVisualAttackStrengthScale() {
        this.visualAttackStrengthTicker = 0;
    }

    @Override
    public void debugify$incrementVisualAttackStrengthScale() {
        this.visualAttackStrengthTicker += 1;
    }
}
