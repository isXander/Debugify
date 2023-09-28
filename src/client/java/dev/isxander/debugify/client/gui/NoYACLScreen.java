package dev.isxander.debugify.client.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;

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
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/yacl"))
                        .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)),
                Component.literal(".minecraft/config/debugify.json").withStyle(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, FabricLoader.getInstance().getConfigDir().toString()))
                        .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)));
    }

    @Override
    protected void init() {
        this.wrappedText = font.split(unwrappedText, width - 50);
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_BACK, button -> minecraft.setScreen(parent))
                        .pos(
                                (this.width - 150) / 2,
                                Mth.clamp(90 + wrappedText.size() * 9 + 12, this.height / 6 + 96, this.height - 24)
                        )
                        .size(150, 20)
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(font, title, width / 2, 70, -1);
        int y = 90;
        for (FormattedCharSequence line : wrappedText) {
            graphics.drawCenteredString(font, line, width / 2, y, -1);
            y += font.lineHeight;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        Style style = getStyle((int) mouseX, (int) mouseY);
        return handleComponentClicked(style);
    }

    protected Style getStyle(int mouseX, int mouseY) {
        int y = mouseY - 90;
        int line = y / font.lineHeight;

        if (y < 0 || y > y + wrappedText.size() * font.lineHeight) return null;
        if (line < 0 || line >= wrappedText.size()) return null;

        FormattedCharSequence text = wrappedText.get(line);
        int x = mouseX - (width / 2 - font.width(text) / 2);

        return font.getSplitter().componentStyleAtWidth(wrappedText.get(line), x);
    }
}
