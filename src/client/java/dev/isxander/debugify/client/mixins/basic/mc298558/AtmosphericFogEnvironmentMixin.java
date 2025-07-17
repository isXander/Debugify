package dev.isxander.debugify.client.mixins.basic.mc298558;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-298558", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Rain fog calculation can overshoot while game is unresponsive")
@Mixin(AtmosphericFogEnvironment.class)
public class AtmosphericFogEnvironmentMixin {
    /**
     * The issue stems from the g * 0.2F, which needs to me Math.min(1.0F, g * 0.2F) instead
     * This mixin replaces the 0.2F with a 1 / g * Math.min(1.0F, g * 0.2F) to leave us with
     * g * 1/g * Math.min(1.0F, g * 0.2F)
     * This may be possible to replace with a better injection (@Expression?)
     */
    @ModifyExpressionValue(method = "setupFog", at = @At(value = "CONSTANT", args = "floatValue=0.2F"))
    private float clampFog(float original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        float delta = deltaTracker.getGameTimeDeltaTicks();
        if (delta == 0.0F) return original;
        return 1 / delta * Math.min(1.0F,  delta * original);
    }
}
