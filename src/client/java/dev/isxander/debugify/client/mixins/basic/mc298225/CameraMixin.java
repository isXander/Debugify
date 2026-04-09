package dev.isxander.debugify.client.mixins.basic.mc298225;

import com.llamalad7.mixinextras.expression.Definition;import com.llamalad7.mixinextras.expression.Expression;import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-298225", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "smoothskies", description = "Shapes appear in the end sky with certain distance settings")
@Mixin(Camera.class)
public class CameraMixin {
    @Definition(id = "depthFar", field = "Lnet/minecraft/client/Camera;depthFar:F")
    @Expression("this.depthFar = @(?)")
    @ModifyExpressionValue(method = "update", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float fixSkyboxFarplaneClipping(float f) {
        return Math.max(f, 11 * 16F); // 11 chunks * 16, the minimum cloud render distance for no clipping issues
    }
}
