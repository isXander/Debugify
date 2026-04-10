package dev.isxander.debugify.client.mixins.basic.mc267376;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-267376", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "You can view through blocks on small scales (near plane clipping)")
@Mixin(Camera.class)
public class CameraMixin {
    @ModifyArg(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setupPerspective(FFFFF)V"
            ),
            index = 0
    )
    private float scaleNearClipDistance(float original, @Local(name = "player") LocalPlayer player) {
        return original * Math.min(player.getScale(), 1F);
    }
}
