package cc.woverflow.debugify.mixins.server.mc90084;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BoatEntity.class)
public class BoatEntityMixin {
    /**
     * This needs to be in server because
     * it's used to update entity position
     *
     * @author isXander
     */
    @Overwrite
    public double getMountedHeightOffset() {
        return 0.0;
    }
}
