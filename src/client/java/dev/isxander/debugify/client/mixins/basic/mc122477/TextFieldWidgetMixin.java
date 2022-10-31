package dev.isxander.debugify.client.mixins.basic.mc122477;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.client.fixes.mc122477.KeyboardPollCounter;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, os = OS.LINUX)
@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
    @Inject(method = "<init>(Lnet/minecraft/client/font/TextRenderer;IIIILnet/minecraft/client/gui/widget/TextFieldWidget;Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    private void startPolling(TextRenderer textRenderer, int x, int y, int width, int height, TextFieldWidget copyFrom, Text text, CallbackInfo ci) {
        KeyboardPollCounter.startCounting();
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Util.getOperatingSystem() == Util.OperatingSystem.LINUX) {
            if (KeyboardPollCounter.getCount() == 1)
                cir.setReturnValue(true);

            if (KeyboardPollCounter.getCount() >= 1)
                KeyboardPollCounter.stopCountingAndReset();
        }
    }
}
