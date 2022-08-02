package dev.isxander.debugify.client.mixins.basic.mc165595;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.render.entity.GuardianEntityRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Taken from Sodium Extra
 * https://github.com/FlashyReese/sodium-extra-fabric
 * under LGLv3 license
 *
 * Adapted to be able to be toggled off at ease in a user-friendly GUI
 *
 * @author AMereBagatelle
 */
@BugFix(id = "MC-165595", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(value = GuardianEntityRenderer.class, priority = 900)
public class GuardianEntityRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/world/World.getTime()J"), expect = 0)
    private long useCorrectTime(World world) {
        return world.getTimeOfDay();
    }
}
