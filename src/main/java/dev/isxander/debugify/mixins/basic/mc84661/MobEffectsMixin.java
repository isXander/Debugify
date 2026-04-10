package dev.isxander.debugify.mixins.basic.mc84661;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-84661", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Glowing is considered a positive effect in potion item tooltips")
@Mixin(MobEffects.class)
public class MobEffectsMixin {
    @Definition(id = "NEUTRAL", field = "Lnet/minecraft/world/effect/MobEffectCategory;NEUTRAL:Lnet/minecraft/world/effect/MobEffectCategory;")
    @Expression("new ?(@(NEUTRAL), 745784)") // this colour refers to bad omen effect exclusively
    @ModifyExpressionValue(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static MobEffectCategory badOmenEffectCategory(MobEffectCategory arg) {
        return MobEffectCategory.HARMFUL;
    }
}
