package cc.woverflow.debugify.mixins.client.mc233042;

import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DirectConnectScreen.class)
public class DirectConnectScreenMixin {
    @Shadow private TextFieldWidget addressField;

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setTextFieldFocused(Z)V"))
    private void unfocusTextField(TextFieldWidget instance, boolean focused) {
        this.addressField.setTextFieldFocused(false);
    }
}
