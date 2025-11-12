package dev.isxander.debugify.mixins.basic.mc147659;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.npc.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-147659", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Some witch huts spawn the incorrect cat")
@Mixin(CatSpawner.class)
public class CatSpawnerMixin {
    @WrapWithCondition(method = "spawnCat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Cat;snapTo(Lnet/minecraft/core/BlockPos;FF)V"))
    private boolean removeOldCatSnap(Cat instance, BlockPos blockPos, float yaw, float pitch) {
        return false;
    }

    @Inject(method = "spawnCat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Cat;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;"))
    private void addNewCatSnap(BlockPos pos, ServerLevel level, boolean persistent, CallbackInfo ci, @Local Cat cat) {
        cat.snapTo(pos, 0, 0);
    }
}
