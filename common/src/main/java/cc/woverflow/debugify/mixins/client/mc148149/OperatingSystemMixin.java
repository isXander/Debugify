package cc.woverflow.debugify.mixins.client.mc148149;

import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Taken from FastOpenLinksAndFolders
 * https://github.com/altrisi/FastOpenLinksAndFolders
 * under LGPLv3 license
 *
 * Adapted to work in a multi-loader environment
 *
 * @author altrisi
 */
@Mixin(Util.OperatingSystem.class)
public abstract class OperatingSystemMixin {
    @Shadow
    public abstract void open(URL url);

    /**
     * @author altrisi
     * @reason Make opening screenshots and chat links non-blocking
     */
    @Overwrite
    public void open(URI uri) {
        CompletableFuture.runAsync(() -> {
            try {
                this.open(uri.toURL());
            } catch (MalformedURLException e) {
                System.err.println("Couldn't open uri '" + uri + "'");
                e.printStackTrace();
            }
        });
    }

    /**
     * @author altrisi
     * @reason Make opening resourcepack and datapacks folder non-blocking
     */
    @Overwrite
    public void open(File file) {
        CompletableFuture.runAsync(() -> {
            try {
                this.open(file.toURI().toURL());
            } catch (MalformedURLException e) {
                System.err.println("Couldn't open file '" + file + "'");
                e.printStackTrace();
            }
        });
    }
}
