package cc.woverflow.debugify.mixins.client.mc145748;

import net.minecraft.client.gui.widget.SliderWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SliderWidget.class)
public abstract class SliderWidgetMixin {

    @Shadow
    protected double value;

    @Unique
    private double startingValue = Double.NaN;

    @Inject(method = "onClick", at = @At("HEAD"))
    private void onClicked(double mouseX, double mouseY, CallbackInfo ci) {
        this.startingValue = this.value;
    }

    @Inject(method = "onDrag", at = @At("HEAD"))
    private void onDrag(double mouseX, double mouseY, double deltaX, double deltaY, CallbackInfo ci) {
        if (Double.isNaN(startingValue)) {
            this.startingValue = this.value;
        }
    }

    @Inject(method = "onRelease", at = @At("HEAD"), cancellable = true)
    private void onRelease(double mouseX, double mouseY, CallbackInfo ci) {
        if (Double.isNaN(startingValue) || startingValue == this.value) {
            ci.cancel();
        }
    }
}
