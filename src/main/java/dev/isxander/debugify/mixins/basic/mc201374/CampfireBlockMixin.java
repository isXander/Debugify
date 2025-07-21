package dev.isxander.debugify.mixins.basic.mc201374;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-201374", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Wrong position passed to getCollisionShape from CampfireBlock#isSmokingBlockAt")
@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {
    @ModifyArg(method = "isSmokeyPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"), index = 1)
    private static BlockPos fixedBlockPos(BlockPos pos, @Local(ordinal = 1) BlockPos blockPos) {
        return blockPos;
    }
}
