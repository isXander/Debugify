package cc.woverflow.debugify.mixins.basic.client.mc147605;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-147605", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ClickableWidget.class)
public abstract class ClickableWidgetMixin {
    @Shadow public abstract boolean isFocused();

    @Inject(method = "isFocused", at = @At("HEAD"), cancellable = true)
    protected void getScreenFocus(CallbackInfoReturnable<Boolean> cir) {

    }

    @ModifyExpressionValue(method = "changeFocus", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;focused:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    protected boolean modifySetFocus(boolean focus) {
        return focus;
    }

    @Inject(method = "changeFocus", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;onFocusedChanged(Z)V", shift = At.Shift.BEFORE))
    protected void setTextFocus(boolean lookForwards, CallbackInfoReturnable<Boolean> cir) {

    }
}
