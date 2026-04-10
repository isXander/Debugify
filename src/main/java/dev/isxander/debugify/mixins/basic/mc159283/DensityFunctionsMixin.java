package dev.isxander.debugify.mixins.basic.mc159283;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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
     * Explicitly cast `x` and `z` to long (from int) to prevent integer overflow when squaring.
     * We use a WrapOperation instead of ModifyArg so we don't do the math twice (one bugged one fixed)
     */
    @Definition(id = "sqrt", method = "Lnet/minecraft/util/Mth;sqrt(F)F")
    @Definition(id = "sectionX", local = @Local(type = int.class, name = "sectionX", argsOnly = true))
    @Definition(id = "sectionZ", local = @Local(type = int.class, name = "sectionZ", argsOnly = true))
    @Expression("sqrt((float) (sectionX * sectionX + sectionZ * sectionZ))")
    @WrapOperation(method = "getHeightValue", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static float castToLongs(float x, Operation<Float> original, SimplexNoise islandNoise, int sectionX, int sectionZ) {
        return Mth.sqrt((long) sectionX * (long) sectionX + (long) sectionZ * (long) sectionZ);
    }
}
