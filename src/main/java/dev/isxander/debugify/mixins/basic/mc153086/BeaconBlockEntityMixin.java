package dev.isxander.debugify.mixins.basic.mc153086;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

/**
 * Taken from a Paper patch attributed to Machine_Maker & BillyGalbreath
 * <a href="https://github.com/PaperMC/Paper/blob/4f79e9eeca8549c72794f53e88c075c1eba46c58/paper-server/patches/sources/net/minecraft/world/entity/ai/goal/EatBlockGoal.java.patch#L7">patch</a>
 * <p>
 * Licensed under MIT
 * <a href="https://github.com/PaperMC/Paper/blob/main/LICENSE.md">license</a>
 */
@BugFix(id = "MC-153086", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Beacons always play deactivating sound when broken, even when not powered")
@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Shadow
    int levels;

    @Shadow
    List<BeaconBeamOwner.Section> beamSections;

    @WrapWithCondition(method = "setRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;playSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;)V"))
    private boolean checkIfActive(Level level, BlockPos pos, SoundEvent sound) {
        return (this.levels > 0 && !this.beamSections.isEmpty());
    }
}
