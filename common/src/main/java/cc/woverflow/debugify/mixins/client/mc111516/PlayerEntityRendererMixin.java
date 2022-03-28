package cc.woverflow.debugify.mixins.client.mc111516;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Taken from https://github.com/TheRandomLabs/RandomPatches under MIT license
 *
 * @author TheRandomLabs
 */
@Mixin(PlayerEntityRenderer.class)
public final class PlayerEntityRendererMixin {
    @Redirect(method = "setupTransforms", at = @At(value = "INVOKE", target = "java/lang/Math.acos(D)D"))
    private double acos(double a) {
        return Math.acos(Math.min(a, 1.0));
    }
}
