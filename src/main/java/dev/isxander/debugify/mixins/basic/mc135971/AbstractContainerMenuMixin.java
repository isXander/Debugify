package dev.isxander.debugify.mixins.basic.mc135971;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-135971", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "carpet")
@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow @Final public NonNullList<Slot> slots;

    @Shadow protected abstract void doClick(int slotIndex, int button, ClickType actionType, Player player);

    @Shadow protected abstract void resetQuickCraft();

    @Shadow public abstract void broadcastChanges();

    @Inject(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;", shift = At.Shift.BEFORE, ordinal = 5), cancellable = true)
    private void handleCtrlQCrafting(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci) {
        Slot slot = slots.get(slotIndex);

        if (slot.hasItem() && slot.mayPickup(player) && slotIndex == 0 && button == 1) {
            Item item = slot.getItem().getItem();

            while (slot.hasItem() && slot.getItem().getItem() == item) {
                doClick(slotIndex, 0, ClickType.THROW, player);
            }

            broadcastChanges();
            resetQuickCraft();
            ci.cancel();
        }
    }
}
