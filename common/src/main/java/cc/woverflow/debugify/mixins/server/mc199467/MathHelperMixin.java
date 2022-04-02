package cc.woverflow.debugify.mixins.server.mc199467;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MathHelper.class)
public class MathHelperMixin {
    /**
     * Without this, large radians from entity ages caused
     * integer overflows. Simply modulo it so that doesn't happen
     */
    @ModifyVariable(method = "cos", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float getRadians(float value) {
        return value % 360;
    }
}
