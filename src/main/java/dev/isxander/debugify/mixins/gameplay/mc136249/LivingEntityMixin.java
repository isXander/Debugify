package dev.isxander.debugify.mixins.gameplay.mc136249;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-136249", category = FixCategory.GAMEPLAY, env = BugFix.Env.SERVER, description = "Wearing boots enchanted with depth strider decreases the strength of the riptide enchantment")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{
    @Shadow
    public abstract boolean isAutoSpinAttack();

    @Definition(id = "getAttributeValue", method = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D")
    @Definition(id = "WATER_MOVEMENT_EFFICIENCY", field = "Lnet/minecraft/world/entity/ai/attributes/Attributes;WATER_MOVEMENT_EFFICIENCY:Lnet/minecraft/core/Holder;")
    @Expression("this.getAttributeValue(WATER_MOVEMENT_EFFICIENCY)")
    @ModifyExpressionValue(method = "travelInWater", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double checkRiptide(double original) {
        return this.isAutoSpinAttack() ? 0 : original;
    }
}
