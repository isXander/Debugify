package dev.isxander.debugify.client.fixes.mc122477;

public class KeyboardPollCounter {
    private static int count = 0;
    private static boolean requiresCount = false;

    public static void poll() {
        if (requiresCount)
            count++;
    }

    public static void startCounting() {
        requiresCount = true;
    }

    public static void stopCountingAndReset() {
        requiresCount = false;
        count = 0;
    }

    public static int getCount() {
        return count;
    }
}
