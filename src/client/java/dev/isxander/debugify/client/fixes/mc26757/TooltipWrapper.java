package dev.isxander.debugify.client.fixes.mc26757;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TooltipWrapper {
    public static List<OrderedText> wrapTooltipLines(Screen screen, TextRenderer textRenderer, List<Text> lines) {
        int width = getMaxWidth(textRenderer, lines);
        int maxWidth = screen.width - 30;
        if (width <= maxWidth)
            return lines.stream().map(Text::asOrderedText).collect(Collectors.toList());

        List<OrderedText> wrapped = new ArrayList<>();
        for (Text line : lines) {
            wrapped.addAll(textRenderer.wrapLines(line, maxWidth));
        }

        return wrapped;
    }

    private static int getMaxWidth(TextRenderer textRenderer, List<Text> lines) {
        int maxWidth = 0;

        for (Text line : lines) {
            int width = textRenderer.getWidth(line);
            if (width > maxWidth)
                maxWidth = width;
        }

        return maxWidth;
    }
}
