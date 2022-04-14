package cc.woverflow.debugify.mixins.client.mc234898;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RealmsMainScreen.class)
public abstract class RealmsMainScreenMixin extends Screen {
    @Shadow private ButtonWidget createTrialButton;

    protected RealmsMainScreenMixin(Text title) {
        super(title);
    }

    @Shadow public abstract boolean shouldShowPopup();

    @Shadow @Final Screen lastScreen;

    @Inject(method = "updateButtonStates", at = @At("RETURN"))
    private void onAddButtons(@Nullable RealmsServer server, CallbackInfo ci) {
        createTrialButton.visible = this.shouldShowPopup();
        createTrialButton.active = this.shouldShowPopup();
    }

    /**
     * avoid checks to see if realms trial
     * is available as it's a faulty check
     */
    @Inject(method = "m_hsfgqbkv", at = @At("HEAD"), cancellable = true)
    private void onPressTrialButton(ButtonWidget button, CallbackInfo ci) {
        Util.getOperatingSystem().open("https://aka.ms/startjavarealmstrial");
        this.client.setScreen(this.lastScreen);
        ci.cancel();
    }
}
