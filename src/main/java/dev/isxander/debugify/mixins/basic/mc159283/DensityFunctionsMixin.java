package dev.isxander.debugify.mixins.basic.mc159283;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Taken from Thorium
 * https://github.com/PotassiumMC/thorium
 * under LGPLv3 license
 *
 * @author NoahvdAa
 */
@BugFix(id = "MC-159283", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "The End terrain does not generate in multiple rings centered around the world center")
@Mixin(DensityFunctions.EndIslandDensityFunction.class)
public class DensityFunctionsMixin {
    /**
     * Explicitly cast `x` and `z` to long (from int) to prevent integer overflow when squaring
     */
    @WrapOperation(method = "getHeightValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sqrt(F)F", ordinal = 0))
    private static float castToLongs(float f, Operation<Float> original, SimplexNoise simplexNoise, int x, int z) {
        return Mth.sqrt((long) x * (long) x + (long) z * (long) z);
    }
}
