package dev.isxander.debugify.mixins.basic.mc263999;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * removeBlock is called before querying the block state of the door,
 * which breaks the level event that causes particles
 */
@BugFix(id = "MC-263999", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Zombies breaking doors do not show break particles")
@Mixin(BreakDoorGoal.class)
public class BreakDoorGoalMixin extends DoorInteractGoal {
    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }

    // Store the block state before removal
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private void storeBlockStatePreRemoval(CallbackInfo ci, @Share("blockState") LocalRef<BlockState> blockStateRef) {
        blockStateRef.set(this.mob.level().getBlockState(this.doorPos));
    }

    // Use the stored block state for the level event
    @Definition(id = "levelEvent", method = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V")
    @Definition(id = "getId", method = "Lnet/minecraft/world/level/block/Block;getId(Lnet/minecraft/world/level/block/state/BlockState;)I")
    @Definition(id = "getBlockState", method = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    @Definition(id = "doorPos", field = "Lnet/minecraft/world/entity/ai/goal/BreakDoorGoal;doorPos:Lnet/minecraft/core/BlockPos;")
    @Expression("?.levelEvent(2001, ?, getId(@(?.getBlockState(?.doorPos))))")
    @WrapOperation(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private BlockState injectCorrectBlockState(Level instance, BlockPos pos, Operation<BlockState> original, @Share("blockState") LocalRef<BlockState> blockStateRef) {
        return blockStateRef.get();
    }
}
