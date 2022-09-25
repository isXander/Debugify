package dev.isxander.debugify.fixes;

import java.util.Locale;

/**
 * Cannot use {@link net.minecraft.util.Util.OperatingSystem} because
 * this code needs to be run from a Mixin plugin, where you can't access
 * Minecraft classes.
 */
public enum OS {
    WINDOWS("debugify.os.windows"),
    MAC("debugify.os.macos"),
    LINUX("debugify.os.linux"),
    SOLARIS("debugify.os.solaris"),
    UNKNOWN("debugify.os.unknown");

    public static OS getOperatingSystem() {
        String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (string.contains("win")) {
            return OS.WINDOWS;
        } else if (string.contains("mac")) {
            return OS.MAC;
        } else if (string.contains("solaris")) {
            return OS.SOLARIS;
        } else if (string.contains("sunos")) {
            return OS.SOLARIS;
        } else if (string.contains("linux")) {
            return OS.LINUX;
        } else {
            return string.contains("unix") ? OS.LINUX : OS.UNKNOWN;
        }
    }

    private final String displayName;

    OS(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
