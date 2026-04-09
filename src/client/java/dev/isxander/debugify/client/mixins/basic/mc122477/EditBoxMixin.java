package dev.isxander.debugify.client.mixins.basic.mc122477;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.client.helpers.mc122477.KeyboardPollCounter;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, os = OS.LINUX, description = "Linux/GNU: Opening chat sometimes writes 't'")
@Mixin(EditBox.class)
public class EditBoxMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/client/gui/Font;IIIILnet/minecraft/client/gui/components/EditBox;Lnet/minecraft/network/chat/Component;)V",
            at = @At("RETURN")
    )
    private void startPolling(CallbackInfo ci) {
        KeyboardPollCounter.startCounting();
    }

    @WrapMethod(method = "charTyped")
    private boolean stopPolling(CharacterEvent event, Operation<Boolean> original) {
        if (Util.getPlatform() == Util.OS.LINUX) {
            if (KeyboardPollCounter.getCount() == 1) {
                return true;
            }

            if (KeyboardPollCounter.getCount() >= 1) {
                KeyboardPollCounter.stopCountingAndReset();
            }
        }

        return original.call(event);
    }
}
