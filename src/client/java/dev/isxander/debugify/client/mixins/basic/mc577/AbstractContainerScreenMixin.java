package dev.isxander.debugify.client.mixins.basic.mc577;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@BugFix(id = "MC-577", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Mouse buttons block all inventory controls that are not default")
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {
    @Shadow
    protected abstract void slotClicked(Slot slot, int slotId, int button, ClickType actionType);

    @Shadow
    @Nullable
    protected abstract Slot getHoveredSlot(double mouseX, double mouseY);

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @ModifyExpressionValue(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"))
    private boolean shouldReturn(boolean parentMouseClicked, MouseButtonEvent event) {
        return parentMouseClicked || debugify$mouseInventoryClose(event);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;getHoveredSlot(DD)Lnet/minecraft/world/inventory/Slot;", shift = At.Shift.AFTER), cancellable = true)
    private void dropWithMouse(MouseButtonEvent event, boolean isDoubleClick, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) boolean isPickedItem) {
        // I am unable to capture this with @Local, so doing this instead
        Slot slot = this.getHoveredSlot(event.x(), event.y());
        if (minecraft.options.keyDrop.matchesMouse(event)) {
            if (slot == null) return;
            slotClicked(slot, slot.index, event.hasControlDown() ? 1 : 0, ClickType.THROW);
            cir.setReturnValue(true);
        }
    }

    @Unique
    private boolean debugify$mouseInventoryClose(MouseButtonEvent event) {
        if (minecraft.options.keyInventory.matchesMouse(event)) {
            onClose();
            return true;
        }

        return false;
    }
}
