package dev.isxander.debugify.mixins.basic.mc231743;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-231743", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin {
    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"))
    private void onIncrementPottedPlantStat(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        player.incrementStat(Stats.USED.getOrCreateStat(player.getStackInHand(hand).getItem()));
    }
}
