package dev.isxander.debugify.mixins.basic.mc168573;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-168573", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "After breaking a shield, the player's off-hand can't finish using some items")
@Mixin(BlocksAttacks.class)
public class BlocksAttacksMixin {
    @Inject(method = "hurtBlockingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
    private void fixShieldBreakItemUse(Level level, ItemStack itemStack, LivingEntity livingEntity, InteractionHand interactionHand, float f, CallbackInfo ci) {
        livingEntity.stopUsingItem();
    }
}
