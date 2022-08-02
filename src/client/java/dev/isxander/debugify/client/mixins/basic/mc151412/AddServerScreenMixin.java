package dev.isxander.debugify.client.mixins.basic.mc151412;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-151412", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(AddServerScreen.class)
public class AddServerScreenMixin extends Screen {
    protected AddServerScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private TextFieldWidget serverNameField;

    /**
     * setTextFieldFocused only makes the prompt appear focused, but does not allow for any text inputs
     * Set this to false and use the correct method
     */
    @Inject(method = "init", at = @At("RETURN"))
    private void setFocus(CallbackInfo ci) {
        this.serverNameField.setTextFieldFocused(false);
        setInitialFocus(serverNameField);
    }
}
