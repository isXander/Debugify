package dev.isxander.debugify.mixins.basic.mc93018;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-93018", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(AnimalEntity.class)
public class AnimalEntityMixin {
    /**
     * Only make wolves love player if they are tamed when fed
     */
    @WrapWithCondition(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;lovePlayer(Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private boolean loveCondition(AnimalEntity animal, PlayerEntity player) {
        if (animal instanceof WolfEntity wolf) {
            return wolf.isTamed();
        }

        return true;
    }
}
