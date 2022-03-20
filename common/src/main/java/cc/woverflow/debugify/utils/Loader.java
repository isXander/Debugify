package cc.woverflow.debugify.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

public enum Loader {
    FABRIC("fabric"),
    FORGE("forge");

    public final String id;

    Loader(String id) {
        this.id = id;
    }

    @ExpectPlatform
    public static Loader getLoader() {
        throw new UnsupportedOperationException();
    }
}
