package dev.isxander.debugify.client.mixins.basic.mc206540;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-206540", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = {"ridingmousefix", "eg_fix_horizontal_camera_lag"}, description = "Increased input delay when riding an entity")
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract float getYRot();

    /**
     * Code taken from {@link net.minecraft.world.entity.vehicle.AbstractBoat}
     */
    @Inject(method = "onPassengerTurned", at = @At("HEAD"))
    private void fixCameraMovement(Entity entity, CallbackInfo ci) {
        if (entity.isAlwaysTicking()) {
            entity.setYBodyRot(this.getYRot());
            float f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
            float g = Mth.clamp(f, -180.0F, 180.0F);
            entity.yRotO += g - f;
            entity.setYRot(entity.getYRot() + g - f);
            entity.setYHeadRot(entity.getYRot());
        }
    }
}
