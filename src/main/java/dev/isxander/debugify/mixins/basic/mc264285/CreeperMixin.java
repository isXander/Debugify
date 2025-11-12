package dev.isxander.debugify.mixins.basic.mc264285;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-264285", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Unbreakable flint and steels are completely consumed when igniting a creeper")
@Mixin(Creeper.class)
public class CreeperMixin {
    /**
     * Igniting a creeper shrinks the stack size by 1 if the item is not damageable.
     * The intention of this was for consumable items which have no durability (has no max damage tag).
     * However, items with the unbreakable tag makes them not damageable, so they get consumed incorrectly.
     * <p>
     * The injection point is within a `!` gate, so the return value is true if the stack is not to be shrunk.
     */
    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
    private boolean shouldNotShrinkStack(ItemStack instance, Operation<Boolean> original) {
        return instance.has(DataComponents.MAX_DAMAGE);
    }
}
