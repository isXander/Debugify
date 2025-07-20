package dev.isxander.debugify.mixins.basic.mc123450;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-123450", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Item frames play sounds when the item within them is read from NBT")
@Mixin(ItemFrame.class)
public class ItemFrameMixin {
    @WrapWithCondition(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private boolean playSound(ItemFrame instance, SoundEvent soundEvent, float volume, float pitch, @Local(argsOnly = true) boolean updateNeighbours) {
        // the targeted method is only given `updateNeighbours = false` when reading from NBT.
        // technically, this means mods who want neighbourless updates will no longer play a sound. 
        // ideally, this would be an additional boolean in the method, but we don't have that privilige.
        return updateNeighbours;
    }
}
