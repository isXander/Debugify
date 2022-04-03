package cc.woverflow.debugify.mixins.client.mc183776;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onSetGamemodeSelectionScreen(int key, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
