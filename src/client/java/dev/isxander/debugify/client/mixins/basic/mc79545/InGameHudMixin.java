package dev.isxander.debugify.client.mixins.basic.mc79545;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@BugFix(id = "MC-79545", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    /**
     * In some cases, this value can wrap-around to negative values
     * This mixin prevents that.
     */
    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getNextLevelExperience()I"))
    private int getNextLevelExperience(ClientPlayerEntity instance) {
        return MathHelper.clamp(instance.getNextLevelExperience(), 1, Integer.MAX_VALUE);
    }
}
