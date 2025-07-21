package dev.isxander.debugify.client.mixins.basic.mc267376;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-267376", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "You can view through blocks on small scales (near plane clipping)")
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyExpressionValue(method = "getProjectionMatrix", at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
    private float scaleNearClipDistance(float original) {
        return original * Math.min(Minecraft.getInstance().player.getScale(), 1F);
    }
}
