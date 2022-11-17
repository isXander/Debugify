package dev.isxander.debugify.mixins.basic.mc199467;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@BugFix(id = "MC-199467", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Mth.class)
public class MthMixin {
    @Shadow @Final public static float TWO_PI;

    /**
     * Without this, large radians from entity ages caused
     * integer overflows. Simply modulo it so that doesn't happen
     * <br>
     * While this bug is reportedly the issue of the client,
     * the root cause is in both environments, hence it's located here
     */
    @ModifyVariable(method = "cos", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyCosRadians(float value) {
        return value % TWO_PI;
    }

    /**
     * Not necessarily needed however it's nice for parity and is affected
     * when <a href="https://bugs.mojang.com/browse/MC-165595">MC-165595</a> is disabled
     */
    @ModifyVariable(method = "sin", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifySinRadians(float value) {
        return value % TWO_PI;
    }
}
