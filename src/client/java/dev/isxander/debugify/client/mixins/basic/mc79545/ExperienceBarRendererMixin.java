package dev.isxander.debugify.client.mixins.basic.mc79545;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-79545", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "The experience bar disappears when too many levels are given to the player")
@Mixin(ExperienceBarRenderer.class)
public class ExperienceBarRendererMixin {
    /**
     * In some cases, this value can wrap-around to negative values
     * This mixin prevents that.
     */
    @ModifyExpressionValue(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXpNeededForNextLevel()I"))
    private int getNextLevelExperience(int nextLevelExperience) {
        return Mth.clamp(nextLevelExperience, 0, Integer.MAX_VALUE);
    }
}
