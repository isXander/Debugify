package dev.isxander.debugify.mixins.gameplay.mc31819;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-31819", category = FixCategory.GAMEPLAY, env = BugFix.Env.SERVER)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "addExhaustion", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"))
    private boolean shouldNotAddExhaustion(boolean isClient) {
        return isClient || world.getDifficulty() == Difficulty.PEACEFUL;
    }
}
