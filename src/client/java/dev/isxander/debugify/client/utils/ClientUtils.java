package dev.isxander.debugify.client.utils;

import net.minecraft.client.Minecraft;

public class ClientUtils {
    public static boolean isInMultiplayerWorld() {
        return !Minecraft.getInstance().isLocalServer() && Minecraft.getInstance().level != null;
    }
}
