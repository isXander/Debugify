package dev.isxander.debugify.client.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

import java.net.URI;

public class NoYACLScreen extends AlertScreen {
    public NoYACLScreen(Screen parent) {
        super(
                () -> Minecraft.getInstance().setScreen(parent),
                Component.translatable("debugify.no_yacl.title").withStyle(ChatFormatting.BOLD),
                Component.translatable("debugify.no_yacl.description",
                        Component.literal("YetAnotherConfigLib").withStyle(style -> style
                                .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://curseforge.com/minecraft/mc-mods/yacl")))
                                .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)),
                        Component.literal(".minecraft/config/debugify.json").withStyle(style -> style
                                .withClickEvent(new ClickEvent.OpenFile(FabricLoader.getInstance().getConfigDir()))
                                .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)))
        );
    }
}
