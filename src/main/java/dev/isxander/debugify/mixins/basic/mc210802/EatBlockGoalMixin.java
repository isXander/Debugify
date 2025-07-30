package dev.isxander.debugify.mixins.basic.mc210802;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Taken from a Paper patch attributed to BillyGalbreath
 * <a href="https://github.com/PaperMC/Paper/blob/4f79e9eeca8549c72794f53e88c075c1eba46c58/paper-server/patches/sources/net/minecraft/world/entity/ai/goal/EatBlockGoal.java.patch#L7">patch</a>
 * <p>
 * Licensed under MIT
 * <a href="https://github.com/PaperMC/Paper/blob/main/LICENSE.md">license</a>
 */
@BugFix(id = "MC-210802", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Inactive sheep eat grass")
@Mixin(EatBlockGoal.class)
public class EatBlockGoalMixin {
    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private Mob mob;

    @WrapMethod(method = "canUse")
    private boolean checkDistance(Operation<Boolean> original) {
        if (!((ChunkMapAccessor) ((ServerLevel) this.level).getChunkSource().chunkMap).getAnyPlayerCloseEnoughForSpawning(this.mob.chunkPosition())) {
            return false;
        }
        return original.call();
    }
}
