package dev.isxander.debugify.mixins.basic.mc160095;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@BugFix(id = "MC-160095", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "carpet-fixes")
@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelReader;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0))
    private BlockState replaceBlockState(LevelReader instance, BlockPos pos) {
        BlockState state = instance.getBlockState(pos);
        // If it is a moving piston we check the block its moving not the moving piston block itself.
        if (state.is(Blocks.MOVING_PISTON) && instance.getBlockEntity(pos) instanceof PistonMovingBlockEntity pistonBlock) {
            return pistonBlock.getMovedState();
        }
        return state;
    }
}
