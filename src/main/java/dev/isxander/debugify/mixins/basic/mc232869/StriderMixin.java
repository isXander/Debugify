package dev.isxander.debugify.mixins.basic.mc232869;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
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

@BugFix(id = "MC-232869", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Adult striders can spawn with saddles in peaceful mode")
@Mixin(Strider.class)
public abstract class StriderMixin extends Animal {
    protected StriderMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Definition(id = "nextInt", method = "Lnet/minecraft/util/RandomSource;nextInt(I)I")
    @Expression("?.nextInt(30) == 0")
    @ModifyExpressionValue(method = "finalizeSpawn", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean preventPeacefulJocky(boolean original) {
        return original && level().getDifficulty() != Difficulty.PEACEFUL;
    }
}
