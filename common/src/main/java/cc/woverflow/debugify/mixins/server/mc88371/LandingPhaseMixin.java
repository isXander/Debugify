package cc.woverflow.debugify.mixins.server.mc88371;

import cc.woverflow.debugify.fixes.BugFix;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.boss.dragon.phase.LandingPhase;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-88371", env = BugFix.Env.SERVER)
@Mixin(LandingPhase.class)
public class LandingPhaseMixin {
    @ModifyExpressionValue(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopPosition(Lnet/minecraft/world/Heightmap$Type;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos getLandingPos(BlockPos pos) {
        if (pos.getY() == 0) {
            // average height of portal
            return pos.withY(65);
        }
        return pos;
    }
}
