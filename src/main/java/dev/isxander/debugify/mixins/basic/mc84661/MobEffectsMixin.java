package dev.isxander.debugify.mixins.basic.mc84661;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-84661", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Glowing is considered a positive effect in potion item tooltips")
@Mixin(MobEffects.class)
public class MobEffectsMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;<init>(Lnet/minecraft/world/effect/MobEffectCategory;I)V", ordinal = 15))
    private static MobEffectCategory badOmenEffectCategory(MobEffectCategory arg) {
        return MobEffectCategory.HARMFUL;
    }
}
