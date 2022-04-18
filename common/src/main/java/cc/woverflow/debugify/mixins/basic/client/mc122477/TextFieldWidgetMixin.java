package cc.woverflow.debugify.mixins.basic.client.mc122477;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@BugFix(id = "MC-122477", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
    @Unique
    private int debugify$ticks = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (debugify$ticks < 2)
            debugify$ticks++;
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (debugify$ticks <= 1 && Util.getOperatingSystem() == Util.OperatingSystem.LINUX)
            cir.setReturnValue(true);
    }
}
