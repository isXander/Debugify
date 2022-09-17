package dev.isxander.debugify.mixins.basic.mc135971;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-135971", category = FixCategory.BASIC, env = BugFix.Env.SERVER, modConflicts = "carpet")
@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Shadow @Final public DefaultedList<Slot> slots;

    @Shadow protected abstract void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player);

    @Shadow protected abstract void endQuickCraft();

    @Shadow public abstract void sendContentUpdates();

    @Inject(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;", shift = At.Shift.BEFORE, ordinal = 5), cancellable = true)
    private void handleCtrlQCrafting(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        Slot slot = slots.get(slotIndex);

        if (slot.hasStack() && slot.canTakeItems(player) && slotIndex == 0 && button == 1) {
            Item item = slot.getStack().getItem();

            while (slot.hasStack() && slot.getStack().getItem() == item) {
                internalOnSlotClick(slotIndex, 0, SlotActionType.THROW, player);
            }

            sendContentUpdates();
            endQuickCraft();
            ci.cancel();
        }
    }
}
