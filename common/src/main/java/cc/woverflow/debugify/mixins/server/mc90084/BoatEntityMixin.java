package cc.woverflow.debugify.mixins.server.mc90084;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public class BoatEntityMixin {
    /**
     * This needs to be in server because
     * it's used to update entity position
     *
     * This should be overwrite but funny architectury
     * bug hahah FIX IT NOW
     *
     * @author isXander
     */
    @Inject(method = "getMountedHeightOffset", at = @At("HEAD"), cancellable = true)
    public void getMountedHeightOffset(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(0.0);
    }
}
