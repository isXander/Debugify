package dev.isxander.debugify.mixins.basic.mc136249;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-136249", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Wearing boots enchanted with depth strider decreases the strength of the riptide enchantment")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{
    @Shadow
    public abstract boolean isAutoSpinAttack();

    @Expression("? = ? + @((0.54600006 - ?) * ?)")
    @ModifyExpressionValue(method = "travelInFluid", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float checkRiptide(float original) {
        return this.isAutoSpinAttack() ? 0 : original;
    }
}
