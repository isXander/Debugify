package dev.isxander.debugify.client.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class NoYACLScreen extends Screen {
    private final Screen parent;

    private final OrderedText title;
    private List<OrderedText> wrappedText;
    private final Text unwrappedText;

    public NoYACLScreen(Screen parent) {
        super(Text.translatable("debugify.no_yacl.title"));
        this.parent = parent;
        this.title = Text.translatable("debugify.no_yacl.title").formatted(Formatting.BOLD).asOrderedText();
        this.unwrappedText = Text.translatable("debugify.no_yacl.description",
                Text.literal("YetAnotherConfigLib").styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/yacl"))
                        .withFormatting(Formatting.BLUE, Formatting.UNDERLINE)),
                Text.literal(".minecraft/config/debugify.json").styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, FabricLoader.getInstance().getConfigDir().toString()))
                        .withFormatting(Formatting.BLUE, Formatting.UNDERLINE)));
    }

    @Override
    protected void init() {
        this.wrappedText = textRenderer.wrapLines(unwrappedText, width - 50);
        this.addDrawableChild(
                new ButtonWidget(
                        (this.width - 150) / 2,
                        MathHelper.clamp(90 + wrappedText.size() * 9 + 12, this.height / 6 + 96, this.height - 24),
                        150, 20,
                        ScreenTexts.BACK,
                        button -> client.setScreen(parent)
                )
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredTextWithShadow(matrices, textRenderer, title, width / 2, 70, -1);
        int y = 90;
        for (OrderedText line : wrappedText) {
            drawCenteredTextWithShadow(matrices, textRenderer, line, width / 2, y, -1);
            y += textRenderer.fontHeight;
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        Style style = getStyle((int) mouseX, (int) mouseY);
        return handleTextClick(style);
    }

    protected Style getStyle(int mouseX, int mouseY) {
        int y = mouseY - 90;
        int line = y / textRenderer.fontHeight;

        if (y < 0 || y > y + wrappedText.size() * textRenderer.fontHeight) return null;
        if (line < 0 || line >= wrappedText.size()) return null;

        OrderedText text = wrappedText.get(line);
        int x = mouseX - (width / 2 - textRenderer.getWidth(text) / 2);

        return textRenderer.getTextHandler().getStyleAt(wrappedText.get(line), x);
    }
}
