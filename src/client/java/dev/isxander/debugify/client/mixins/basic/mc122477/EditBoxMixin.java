package dev.isxander.debugify.client.mixins.basic.mc122477;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.client.helpers.mc122477.KeyboardPollCounter;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, os = OS.LINUX)
@Mixin(EditBox.class)
public class EditBoxMixin {
    @Inject(method = "<init>(Lnet/minecraft/client/gui/Font;IIIILnet/minecraft/client/gui/components/EditBox;Lnet/minecraft/network/chat/Component;)V", at = @At("RETURN"))
    private void startPolling(Font textRenderer, int x, int y, int width, int height, EditBox copyFrom, Component text, CallbackInfo ci) {
        KeyboardPollCounter.startCounting();
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Util.getPlatform() == Util.OS.LINUX) {
            if (KeyboardPollCounter.getCount() == 1)
                cir.setReturnValue(true);

            if (KeyboardPollCounter.getCount() >= 1)
                KeyboardPollCounter.stopCountingAndReset();
        }
    }
}
