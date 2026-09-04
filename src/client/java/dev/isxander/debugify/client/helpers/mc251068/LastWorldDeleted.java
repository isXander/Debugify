package dev.isxander.debugify.client.helpers.mc251068;

public class LastWorldDeleted {
    private static boolean marked = false;

    public static void mark() {
        marked = true;
    }

    public static boolean consume() {
        boolean wasMarked = marked;
        marked = false;
        return wasMarked;
    }
}
