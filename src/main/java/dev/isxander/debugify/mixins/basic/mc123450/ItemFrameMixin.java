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
    private boolean playSound(ItemFrame instance, SoundEvent soundEvent, float volume, float pitch, @Local(argsOnly = true) boolean bl) {
        return bl;
    }
}
