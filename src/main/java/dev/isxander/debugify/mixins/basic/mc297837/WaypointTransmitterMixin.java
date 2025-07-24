package dev.isxander.debugify.mixins.basic.mc297837;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-297837", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Locator bar shows players above you as being below you when they are high enough")
@Mixin(WaypointTransmitter.class)
public class WaypointTransmitterMixin {
    /**
     * {@link WaypointTransmitter#isReallyFar(LivingEntity, ServerPlayer)} was being used to determine whether
     * waypoints should use azimuth distance (far away) or chunk/block distance (close/closer by).
     * <p>
     * The azimuth distance provides only relative yRot, which means the pitch uses the horizon,
     * not the waypoint itself.
     * <p>
     * Even when the waypoint is in the same chunk as the entity, the azimuth distance was used because it was
     * over 332 blocks away, just vertically, not horizontally, as I assume it was intended.
     * <p>
     * This mixin fixes the bug by using the block distance instead of the azimuth distance,
     * by changing the distance calculation to only use horizontal distance.
     */
    @WrapOperation(method = "isReallyFar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;distanceTo(Lnet/minecraft/world/entity/Entity;)F"))
    private static float useOnlyHorizontalDistance(LivingEntity entity1, Entity entity2, Operation<Float> original) {
        double xDist = entity1.getX() - entity2.getX();
        double zDist = entity1.getZ() - entity2.getZ();
        return (float) Math.hypot(xDist, zDist);
    }

}
