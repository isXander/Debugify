package dev.isxander.debugify.mixins.basic.mc232869;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-232869", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(Strider.class)
public abstract class StriderMixin extends Animal {
    protected StriderMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0))
    private int preventPeacefulJockey(int random) {
        // non-zero doesn't spawn jockey
        return level.getDifficulty() == Difficulty.PEACEFUL ? 1 : random;
    }
}
