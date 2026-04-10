package dev.isxander.debugify.mixins.basic.mc298066;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-298066", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Directly entering a bed from a mount places the player in the wrong place")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // This is just a hacky fix to force the player into the correct position
    @Inject(method = "setPosToBed", at = @At("TAIL"))
    private void teleportToBed(BlockPos bedPosition, CallbackInfo ci) {
        this.teleportTo(bedPosition.getX() + 0.5, bedPosition.getY() + 0.6875, bedPosition.getZ() + 0.5);
    }
}
