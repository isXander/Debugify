package cc.woverflow.debugify.mixins.client.mc108948;

import cc.woverflow.debugify.fixes.BugFix;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-108948", env = BugFix.Env.CLIENT)
@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * when the boat is being handled by client, and the player isn't controlling it,
     * it's movement is not calculated. creating de-sync between client and server.
     */
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;isLogicalSideForUpdatingMovement()Z"))
    private boolean shouldUpdateVelocity(boolean isLogicalSideForUpdatingMovement) {
        return true;
    }

    /**
     * still limit packets and stuff like that if not controlled because
     * there is no need to send packets if the player isn't controlling it.
     */
    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z", ordinal = 1, opcode = Opcodes.GETFIELD))
    private boolean updatePaddles(boolean isClient) {
        return isClient && isLogicalSideForUpdatingMovement();
    }
}
