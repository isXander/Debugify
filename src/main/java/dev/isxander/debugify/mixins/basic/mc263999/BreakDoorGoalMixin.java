package dev.isxander.debugify.mixins.basic.mc263999;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-263999", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Zombies breaking doors do not show break particles")
@Mixin(BreakDoorGoal.class)
public class BreakDoorGoalMixin extends DoorInteractGoal {
    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }

    /**
     * Minecraft removes the door block before finding the door's position.
     * Instead, we make a copy of the block state prior to removing the door block to supply the block state check
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private void getOldBlockState(CallbackInfo ci, @Share("doorBlockState") LocalRef<BlockState> blockStateRef) {
        blockStateRef.set(this.mob.level().getBlockState(this.doorPos));
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState setBlockState(BlockState original, @Share("doorBlockState") LocalRef<BlockState> blockStateRef) {
        return blockStateRef.get();
    }
}
