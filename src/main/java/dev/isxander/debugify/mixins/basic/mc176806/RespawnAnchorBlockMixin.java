package dev.isxander.debugify.mixins.basic.mc176806;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-176806", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Scoreboard criteria for using glowstone doesn't increase score when charging a respawn anchor")
@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {
    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void awardGlowstoneStat(
            CallbackInfoReturnable<InteractionResult> cir,
            @Local(argsOnly = true, name = "itemStack") ItemStack itemStack,
            @Local(argsOnly = true, name = "player") Player player
    ) {
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
    }
}
