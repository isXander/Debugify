package dev.isxander.debugify.mixins.gameplay.mc31819;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-31819", category = FixCategory.GAMEPLAY, env = BugFix.Env.SERVER)
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "causeFoodExhaustion", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z"))
    private boolean shouldNotAddExhaustion(boolean isClient) {
        return isClient || level().getDifficulty() == Difficulty.PEACEFUL;
    }
}
