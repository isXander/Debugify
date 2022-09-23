package dev.isxander.debugify.client.mixins.basic.mc147605.jeicompat;

import dev.isxander.debugify.client.mixins.basic.mc147605.TextFieldWidgetMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * JEI uses its own focus handling.
 * This mixin simply gets rid of all the custom behaviour.
 */
@Pseudo
@Mixin(targets = "mezz.jei.common.input.GuiTextFieldFilter")
public abstract class GuiTextFieldFilterMixin extends TextFieldWidgetMixin {
    @Override
    protected void focusSelf(boolean focused, CallbackInfo ci) {

    }

    @Override
    protected void getScreenFocus(CallbackInfoReturnable<Boolean> cir) {

    }

    @Override
    protected boolean modifyChangeFocus(boolean focus) {
        return focus;
    }

    @Override
    protected void changeFocus(boolean lookForwards, CallbackInfoReturnable<Boolean> cir) {

    }
}
