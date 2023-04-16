package dev.isxander.debugify.client.mixins.basic.mc140646;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Taken from <a href="https://github.com/j-tai/TieFix">TieFix</a>
 * under LGPLv3 license
 * <br>
 * Adapted into the Debugify mod. The one-stop shop to all fixes and only fixes.
 *
 * @author j-tai
 */
@BugFix(id = "MC-140646", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(EditBox.class)
public abstract class EditBoxMixin {
    @Shadow
    private boolean shiftPressed;
    @Shadow
    private int highlightPos;

    @Shadow
    public abstract void setHighlightPos(int index);

    /**
     * Scrolling logic is in {@link EditBox#setHighlightPos} so we call it
     * and don't let the method actually modify the selectionEnd
     */
    @Inject(method = "moveCursorTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setCursorPosition(I)V", shift = At.Shift.AFTER))
    private void onSetCursor(int cursor, CallbackInfo ci) {
        if (shiftPressed) {
            int end = highlightPos;
            setHighlightPos(cursor);
            highlightPos = end;
        }
    }

    /**
     * Prevent the substring end index being less than 0,
     * causing a crash.
     */
    @ModifyArg(method = "renderWidget", at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(II)Ljava/lang/String;", ordinal = 1), index = 1)
    private int boundSelectionEnd(int relativeSelectionEnd) {
        return Math.max(0, relativeSelectionEnd);
    }
}
