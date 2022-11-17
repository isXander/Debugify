package dev.isxander.debugify.client.mixins.basic.mc183776;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-183776", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method = "handleDebugKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onSetGamemodeSelectionScreen(int key, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
