package dev.isxander.debugify.client.utils;

import net.minecraft.client.MinecraftClient;

public class ClientUtils {
    public static boolean isInMultiplayerWorld() {
        return !MinecraftClient.getInstance().isInSingleplayer() && MinecraftClient.getInstance().world != null;
    }
}
