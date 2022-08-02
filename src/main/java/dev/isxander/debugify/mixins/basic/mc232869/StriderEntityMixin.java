package dev.isxander.debugify.mixins.basic.mc232869;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-232869", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity {
    protected StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 0))
    private int preventPeacefulJockey(int random) {
        // non-zero doesn't spawn jockey
        return world.getDifficulty() == Difficulty.PEACEFUL ? 1 : random;
    }
}
