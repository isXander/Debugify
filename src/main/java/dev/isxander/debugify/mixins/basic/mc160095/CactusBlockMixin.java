package dev.isxander.debugify.mixins.basic.mc160095;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-160095", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "carpet-fixes", description = "Partial blocks break cactus when moved by pistons")
@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @Definition(id = "getBlockState", method = "Lnet/minecraft/world/level/LevelReader;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    @Definition(id = "pos", local = @Local(type = BlockPos.class, name = "pos", argsOnly = true))
    @Definition(id = "relative", method = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;")
    @Definition(id = "direction", local = @Local(type = Direction.class, name = "direction"))
    @Expression("?.getBlockState(pos.relative(direction))")
    @WrapOperation(method = "canSurvive", at = @At("MIXINEXTRAS:EXPRESSION"))
    private BlockState replaceBlockState(LevelReader instance, BlockPos blockPos, Operation<BlockState> original) {
        BlockState state = original.call(instance, blockPos);
        // If it is a moving piston we check the block its moving not the moving piston block itself.
        if (state.is(Blocks.MOVING_PISTON) && instance.getBlockEntity(blockPos) instanceof PistonMovingBlockEntity pistonBlock) {
            return pistonBlock.getMovedState();
        }
        return state;
    }
}
