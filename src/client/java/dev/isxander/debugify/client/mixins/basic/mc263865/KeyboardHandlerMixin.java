package dev.isxander.debugify.client.mixins.basic.mc263865;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-263865", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "keyPress",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;set(Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;fullscreen()Lnet/minecraft/client/OptionInstance;"))
    )
    private void saveOptionsOnFullscreen(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        this.minecraft.options.save();
    }
}
