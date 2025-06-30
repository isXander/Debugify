package dev.isxander.debugify.client.mixins.basic.mc176559;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.item.ItemStack;

@BugFix(id = "MC-176559", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Breaking process resets when a pickaxe enchanted with Mending mends by XP / Mending slows down breaking blocks again")
@Mixin(value = MultiPlayerGameMode.class, priority = 1010)
public class MultiPlayerGameModeMixin {
    // Fabric API also redirects here. WrapOperation is compatible
    @WrapOperation(method = "sameDestroyTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean isSameItem(ItemStack mainHandItem, ItemStack destroyingItem, Operation<Boolean> original) {
        return isSameItemSameComponentsIgnoringDurability(mainHandItem, destroyingItem);
    }

    @Unique
    private boolean isSameItemSameComponentsIgnoringDurability(ItemStack stack1, ItemStack stack2) {
        if (!stack1.is(stack2.getItem())) {
            return false;
        } else if (stack1.isEmpty() && stack2.isEmpty()) {
            return true;
        } else {
            int damage1 = stack1.getDamageValue();
            int damage2 = stack2.getDamageValue();
            stack1.setDamageValue(0);
            stack2.setDamageValue(0);

            PatchedDataComponentMap components1 = ((ItemStackAccessor) (Object) stack1).getComponents();
            PatchedDataComponentMap components2 = ((ItemStackAccessor) (Object) stack2).getComponents();
            boolean comparison = Objects.equals(components1, components2);

            stack1.setDamageValue(damage1);
            stack2.setDamageValue(damage2);

            return comparison;
        }
    }
}
