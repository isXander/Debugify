package dev.isxander.debugify.client.mixins.basic.mc577;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@BugFix(id = "MC-577", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
    @Shadow protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @ModifyExpressionValue(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseClicked(DDI)Z"))
    private boolean shouldReturn(boolean parentMouseClicked, double mouseX, double mouseY, int button) {
        return parentMouseClicked || mouseInventoryClose(button);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void dropWithMouse(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir, boolean isPickItem, Slot hoveredSlot) {
        if (client.options.dropKey.matchesMouse(button)) {
            onMouseClick(hoveredSlot, hoveredSlot.id, hasControlDown() ? 1 : 0, SlotActionType.THROW);
            cir.setReturnValue(true);
        }
    }

    private boolean mouseInventoryClose(int button) {
        if (client.options.inventoryKey.matchesMouse(button)) {
            close();
            return true;
        }

        return false;
    }
}
