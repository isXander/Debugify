package cc.woverflow.debugify.mixins.basic.client.mc147605;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import cc.woverflow.debugify.fixes.mc147605.TextFieldHolder;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@BugFix(id = "MC-147605", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(AbstractParentElement.class)
public class AbstractParentElementMixin implements TextFieldHolder {
    @Unique @Nullable
    public TextFieldWidget debugify$focusedTextWidget = null;


    @Override
    public @Nullable TextFieldWidget getFocusedTextField() {
        return debugify$focusedTextWidget;
    }

    @Override
    public void setFocusedTextField(@Nullable TextFieldWidget widget) {
        debugify$focusedTextWidget = widget;
    }
}
