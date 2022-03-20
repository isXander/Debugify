package cc.woverflow.debugify.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class ExpectUtils {
    @ExpectPlatform
    public static Path getConfigPath() {
        throw new UnsupportedOperationException();
    }
}
