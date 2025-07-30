package dev.isxander.debugify.mixins.basic.mc264285;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-264285", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Unbreakable flint and steels are completely consumed when igniting a creeper")
@Mixin(Creeper.class)
public class CreeperMixin {
    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
    private boolean fixUnbreakableItems(ItemStack instance, Operation<Boolean> original) {
        return instance.getMaxDamage() != 0;
    }
}
