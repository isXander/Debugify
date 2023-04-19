package dev.isxander.debugify.mixinplugin;

public class DebugifyDebugFlags {
    public static final boolean FORCE_LINUX_FIXES = boolProp("debugify.forceLinuxFixes");
    public static final boolean FORCE_WINDOWS_FIXES = boolProp("debugify.forceWindowsFixes");
    public static final boolean FORCE_MACOS_FIXES = boolProp("debugify.forceMacOSFixes");

    private static boolean boolProp(String property) {
        return Boolean.parseBoolean(System.getProperty(property));
    }
}
