package dev.isxander.debugify.mixins.basic.mc90084;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-90084", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(BoatEntity.class)
public class BoatEntityMixin {
    /**
     * @author isXander
     * @reason fix bug
     */
    @Overwrite
    public double getMountedHeightOffset() {
        return 0.0;
    }
}
