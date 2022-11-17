package dev.isxander.debugify.client.helpers.mc147605;

import net.minecraft.client.gui.components.EditBox;
import org.jetbrains.annotations.Nullable;

public interface TextFieldHolder {
    @Nullable
    EditBox getFocusedTextField();

    void setFocusedTextField(@Nullable EditBox widget);
}
