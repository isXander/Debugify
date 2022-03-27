package cc.woverflow.debugify.mixins.client.mc165595;

import net.minecraft.client.render.entity.GuardianEntityRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Taken from a closed, unmerged Sodium PR under LGPLv3 License
 * https://github.com/AMereBagatelle/sodium-fabric/tree/guardian-beam-fix
 * @author AMereBagatelle
 */
@Mixin(value = GuardianEntityRenderer.class, priority = 900)
public class GuardianEntityRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/world/World.getTime()J"), expect = 0)
    private long useCorrectTime(World world) {
        return world.getTimeOfDay();
    }
}
