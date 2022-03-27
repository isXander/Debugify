package cc.woverflow.debugify.mixins.client.mc79545;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    /**
     * In some cases, this value can wrap-around to negative values
     * This mixin prevents that.
     */
    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getNextLevelExperience()I"))
    private int getNextLevelExperience(ClientPlayerEntity instance) {
        //Technically this only ever needs to be 1 and doesn't need to be anything else than 1 as it's only used for the if statement
        //but just in case if Mojang in the future decide it will be used for something else then 1 just clamp it.
        return MathHelper.clamp(instance.getNextLevelExperience(), 1, Integer.MAX_VALUE);
    }
}
