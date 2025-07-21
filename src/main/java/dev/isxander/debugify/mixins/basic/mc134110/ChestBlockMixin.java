package dev.isxander.debugify.mixins.basic.mc134110;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import static net.minecraft.world.level.block.ChestBlock.TYPE;

@BugFix(id = "MC-134110", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Structure mirroring breaking apart double chests")
@Mixin(ChestBlock.class)
public class ChestBlockMixin {
    @WrapMethod(method = "mirror")
    private BlockState fixChestRotations(BlockState blockState, Mirror mirror, Operation<BlockState> original) {
        BlockState result = original.call(blockState, mirror);
        return mirror == Mirror.NONE ? result : result.setValue(TYPE, result.getValue(TYPE).getOpposite());
    }
}
