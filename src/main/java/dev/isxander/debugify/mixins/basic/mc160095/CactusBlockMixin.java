package dev.isxander.debugify.mixins.basic.mc160095;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@BugFix(id = "MC-160095", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0))
    private BlockState replaceBlockState(WorldView instance, BlockPos pos) {
        BlockState state = instance.getBlockState(pos);
        //If it is a moving piston we check the block its moving not the moving piston block itself.
        if (state.isOf(Blocks.MOVING_PISTON) && instance.getBlockEntity(pos) instanceof PistonBlockEntity pistonBlock) {
            return pistonBlock.getPushedBlock();
        }
        return state;
    }
}
