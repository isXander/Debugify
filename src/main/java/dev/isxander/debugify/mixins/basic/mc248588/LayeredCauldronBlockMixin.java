package dev.isxander.debugify.mixins.basic.mc248588;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Taken from a Paper patch attributed to Machine_Maker
 * <a href="https://github.com/PaperMC/Paper/blob/4f79e9eeca8549c72794f53e88c075c1eba46c58/paper-server/patches/sources/net/minecraft/world/level/block/LayeredCauldronBlock.java.patch#L13">patch</a>
 * <p>
 * Licensed under MIT
 * <a href="https://github.com/PaperMC/Paper/blob/main/LICENSE.md">license</a>
 */
@BugFix(id = "MC-248588", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "The \"mobGriefing\" gamerule doesn't prevent burning entities from being able to decrease the levels of water or powder snow cauldrons")
@Mixin(LayeredCauldronBlock.class)
public class LayeredCauldronBlockMixin {
    @WrapOperation(method = "method_71627", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;mayInteract(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean checkMobGriefing(Entity instance, ServerLevel level, BlockPos pos, Operation<Boolean> original) {
        return (instance instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && original.call(instance, level, pos);
    }
}
