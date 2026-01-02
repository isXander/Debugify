package dev.isxander.debugify.client.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.net.URI;
import java.util.List;
import org.jspecify.annotations.Nullable;

public class NoYACLScreen extends Screen {
    private final Screen parent;

    private final FormattedCharSequence title;
    private List<FormattedCharSequence> wrappedText;
    private final Component unwrappedText;

    public NoYACLScreen(Screen parent) {
        super(Component.translatable("debugify.no_yacl.title"));
        this.parent = parent;
        this.title = Component.translatable("debugify.no_yacl.title").withStyle(ChatFormatting.BOLD).getVisualOrderText();
        this.unwrappedText = Component.translatable("debugify.no_yacl.description",
                Component.literal("YetAnotherConfigLib").withStyle(style -> style
                        .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://curseforge.com/minecraft/mc-mods/yacl")))
                        .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)),
                Component.literal(".minecraft/config/debugify.json").withStyle(style -> style
                        .withClickEvent(new ClickEvent.OpenFile(FabricLoader.getInstance().getConfigDir()))
                        .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)));
    }

    @Override
    protected void init() {
        this.wrappedText = this.font.split(unwrappedText, width - 50);
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_BACK, button -> minecraft.setScreen(parent))
                        .pos(
                                (this.width - 150) / 2,
                                Mth.clamp(90 + this.wrappedText.size() * 9 + 12, this.height / 6 + 96, this.height - 24)
                        )
                        .size(150, 20)
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(this.font, title, this.width / 2, 70, -1);
        int y = 90;
        for (FormattedCharSequence line : wrappedText) {
            graphics.drawCenteredString(this.font, line, this.width / 2, y, -1);
            y += this.font.lineHeight;
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (super.mouseClicked(mouseButtonEvent, bl)) {
            return true;
        }

        Style style = this.getStyle((int) mouseButtonEvent.x(), (int) mouseButtonEvent.y());
        return this.handleClickEvent(style.getClickEvent());
    }

    protected Style getStyle(int mouseX, int mouseY) {
        int y = mouseY - 90;
        int line = y / this.font.lineHeight;

        if (y < 0 || y > y + this.wrappedText.size() * this.font.lineHeight) return null;
        if (line < 0 || line >= this.wrappedText.size()) return null;

        FormattedCharSequence text = this.wrappedText.get(line);
        int x = mouseX - (this.width / 2 - this.font.width(text) / 2);

        ActiveTextCollector.ClickableStyleFinder clickableStyleFinder = new ActiveTextCollector.ClickableStyleFinder(this.font, mouseX, mouseY);
        return clickableStyleFinder.result();
    }

    protected boolean handleClickEvent(@Nullable ClickEvent clickEvent) {
        if (clickEvent == null) {
            return false;
        }

        defaultHandleGameClickEvent(clickEvent, this.minecraft, this);
        return true;
    }
}
