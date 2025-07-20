package dev.isxander.debugify.client.mixins.basic.mc298558;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-298558", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Rain fog calculation can overshoot while game is unresponsive")
@Mixin(AtmosphericFogEnvironment.class)
public class AtmosphericFogEnvironmentMixin {

    /**
     * The issue stems from the gtDeltaTicks * 0.2, which needs a maximum value of 1.
     */
    @Definition(id = "rainFogMultiplier", field = "Lnet/minecraft/client/renderer/fog/environment/AtmosphericFogEnvironment;rainFogMultiplier:F")
    @Definition(id = "rainLevel", local = @Local(type = float.class, ordinal = 3))
    @Definition(id = "gtDeltaTicks", local = @Local(type = float.class, ordinal = 1))
    // `?.` instead of `this.` since the `this` is DUPed making it impossible to target
    @Expression("?.rainFogMultiplier = ?.rainFogMultiplier + (rainLevel - ?.rainFogMultiplier) * @(gtDeltaTicks) * 0.2")
    @ModifyExpressionValue(method = "setupFog", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float clampFog(float original) {
        // it's impossible to target `gtDeltaTicks * 0.2` since in bytecode
        // it's `...) * gtDeltaTicks) * 0.2`. so instead just modify `gtDeltaTicks` with min 5, since 5 * 0.2 is 1
        return Math.min(5f, original);
    }
}
