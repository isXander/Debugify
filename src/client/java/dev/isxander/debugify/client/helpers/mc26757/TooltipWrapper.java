package dev.isxander.debugify.client.helpers.mc26757;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class TooltipWrapper {
    public static List<FormattedCharSequence> wrapTooltipLines(Screen screen, Font textRenderer, List<Component> lines) {
        int width = getMaxWidth(textRenderer, lines);
        int maxWidth = screen.width - 30;
        if (width <= maxWidth)
            return lines.stream().map(Component::getVisualOrderText).collect(Collectors.toList());

        List<FormattedCharSequence> wrapped = new ArrayList<>();
        for (Component line : lines) {
            wrapped.addAll(textRenderer.split(line, maxWidth));
        }

        return wrapped;
    }

    private static int getMaxWidth(Font textRenderer, List<Component> lines) {
        int maxWidth = 0;

        for (Component line : lines) {
            int width = textRenderer.width(line);
            if (width > maxWidth)
                maxWidth = width;
        }

        return maxWidth;
    }
}
