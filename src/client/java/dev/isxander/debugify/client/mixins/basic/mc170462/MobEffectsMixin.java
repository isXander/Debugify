package dev.isxander.debugify.client.mixins.basic.mc170462;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-170462", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Bad Omen is considered a positive effect in potion item tooltips")
@Mixin(MobEffects.class)
public class MobEffectsMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/BadOmenMobEffect;<init>(Lnet/minecraft/world/effect/MobEffectCategory;I)V"))
    private static MobEffectCategory badOmenEffectCategory(MobEffectCategory arg) {
        return MobEffectCategory.HARMFUL;
    }
}
