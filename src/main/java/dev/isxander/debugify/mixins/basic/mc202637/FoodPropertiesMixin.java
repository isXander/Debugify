package dev.isxander.debugify.mixins.basic.mc202637;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-202637", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Last sound clip of eating will still play when Players volume is set to 0%")
@Mixin(FoodProperties.class)
public class FoodPropertiesMixin {

    @Definition(id = "playSound", method = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V")
    @Definition(id = "consumable", local = @Local(type = Consumable.class, name = "consumable", argsOnly = true))
    @Definition(id = "sound", method = "Lnet/minecraft/world/item/component/Consumable;sound()Lnet/minecraft/core/Holder;")
    @Expression("?.playSound(?, ?, ?, ?, (?) consumable.sound().?(), ?, ?, ?)")
    @ModifyArg(method = "onConsume", at = @At("MIXINEXTRAS:EXPRESSION"))
    private SoundSource modifyEatSoundSource(SoundSource soundSource) {
        return SoundSource.PLAYERS;
    }
}
