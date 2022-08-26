package dev.isxander.debugify.client.mixins.basic.mc111516;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Taken from <a href="https://github.com/TheRandomLabs/RandomPatches">RandomPatches</a>
 * under MIT license
 * <br>
 * Adapted to work in newer versions and a multi-loader environment
 *
 * @author TheRandomLabs
 */
@BugFix(id = "MC-111516", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public final class PlayerEntityRendererMixin {
    @Redirect(method = "setupTransforms", at = @At(value = "INVOKE", target = "java/lang/Math.acos(D)D"))
    private double acos(double a) {
        return Math.acos(Math.min(a, 1.0));
    }
}
