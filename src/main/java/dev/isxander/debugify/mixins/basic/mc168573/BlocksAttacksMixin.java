package dev.isxander.debugify.mixins.basic.mc168573;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-168573", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "After breaking a shield, the player's off-hand can't finish using some items")
@Mixin(BlocksAttacks.class)
public class BlocksAttacksMixin {
    /**
     * We need to check if the shield is going to break and only stop using the item after it is broken.
     * We can't use isBroken here, so instead we check if the next damage will break the blockable, then do the damage call after.
     */
    @WrapOperation(
            method = "hurtBlockingItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
            )
    )
    private void fixShieldBreakItemUse(
            ItemStack instance,
            int amount,
            LivingEntity owner,
            EquipmentSlot slot,
            Operation<Void> original,
            @Local(argsOnly = true, name = "item") ItemStack item
    ) {
        boolean isBroken = item.nextDamageWillBreak();

        original.call(instance, amount, owner, slot);

        if (isBroken) {
            owner.stopUsingItem();
        }
    }
}
