package dev.isxander.debugify.client.mixins.basic.mc145748;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.widget.SliderWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-145748", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(SliderWidget.class)
public class SliderWidgetMixin {
    @Unique
    private boolean debugify$clicked = false;

    /**
     * the issue occurs because mouseRelease event
     * is fired when it was down on the previous screen.
     * <br>
     * track the first value which will show that the
     * slider has been clicked at least once
     */
    @Inject(method = "onClick", at = @At("HEAD"))
    private void onClicked(double mouseX, double mouseY, CallbackInfo ci) {
        this.debugify$clicked = true;
    }

    /**
     * same here
     */
    @Inject(method = "onDrag", at = @At("HEAD"))
    private void onDrag(double mouseX, double mouseY, double deltaX, double deltaY, CallbackInfo ci) {
        if (!debugify$clicked) {
            this.debugify$clicked = true;
        }
    }

    /**
     * don't trigger mouse release if the mouse has never been down on the slider
     */
    @Inject(method = "onRelease", at = @At("HEAD"), cancellable = true)
    private void onRelease(double mouseX, double mouseY, CallbackInfo ci) {
        if (!debugify$clicked) {
            ci.cancel();
        }
    }
}
