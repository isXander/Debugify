package dev.isxander.debugify.mixins.basic.mc202637;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-202637", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Last sound clip of eating will still play when Players volume is set to 0%")
@Mixin(FoodProperties.class)
public class FoodPropertiesMixin {
    @ModifyArg(method = "onConsume", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 0), index = 5)
    private SoundSource modifyEatSoundSource(SoundSource soundSource) {
        return SoundSource.PLAYERS;
    }
}
