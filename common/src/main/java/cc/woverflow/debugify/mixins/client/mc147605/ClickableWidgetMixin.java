package cc.woverflow.debugify.mixins.client.mc147605;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClickableWidget.class)
public class ClickableWidgetMixin {
    @Inject(method = "isFocused", at = @At("HEAD"), cancellable = true)
    protected void getScreenFocus(CallbackInfoReturnable<Boolean> cir) {

    }
}
