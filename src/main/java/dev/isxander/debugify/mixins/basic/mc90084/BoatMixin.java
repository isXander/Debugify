package dev.isxander.debugify.mixins.basic.mc90084;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@BugFix(id = "MC-90084", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Boat.class)
public class BoatMixin {
    @ModifyConstant(method = "getPassengersRidingOffset", constant = @Constant(doubleValue = -0.1))
    private double modifyRegularBoatHeight(double offset) {
        return 0.0;
    }
}
