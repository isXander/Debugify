package cc.woverflow.debugify.mixins.client.mc147605;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.mc147605.TextFieldHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Most GUIs only have one text field so this actually quite bad
 * bug doesn't show itself. The text field is focused in the mouseClick event,
 * but it doesn't ever unfocus any other text fields.
 *
 * Cannot use the screen's focused field either as that is primarily used
 * for widgets which breaks many guis with text fields in widgets
 */
@BugFix(id = "MC-147605", env = BugFix.Env.CLIENT)
@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin extends ClickableWidgetMixin {
    @Inject(method = "setTextFieldFocused", at = @At("HEAD"))
    private void focusSelf(boolean focused, CallbackInfo ci) {
        if (focused && MinecraftClient.getInstance().currentScreen != null) {
            ((TextFieldHolder) MinecraftClient.getInstance().currentScreen).setFocusedTextField((TextFieldWidget) (Object) this);
        }
    }

    @Override
    protected void getScreenFocus(CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            cir.setReturnValue(((Object) this).equals(((TextFieldHolder) MinecraftClient.getInstance().currentScreen).getFocusedTextField()));
        }
    }
}
