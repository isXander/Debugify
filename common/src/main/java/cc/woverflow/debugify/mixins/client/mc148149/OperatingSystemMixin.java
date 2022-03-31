package cc.woverflow.debugify.mixins.client.mc148149;

import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
     *
     * should be using @Overwrite but architectury doesn't like that
     */
    @Inject(method = "open(Ljava/net/URI;)V", at = @At("HEAD"), cancellable = true)
    public void openUri(URI uri, CallbackInfo ci) {
        CompletableFuture.runAsync(() -> {
            try {
                this.open(uri.toURL());
            } catch (MalformedURLException e) {
                System.err.println("Couldn't open uri '" + uri + "'");
                e.printStackTrace();
            }
        });

        ci.cancel();
    }

    /**
     * @author altrisi
     * @reason Make opening resourcepack and datapacks folder non-blocking
     *
     * should be using @Overwrite but architectury doesn't like that
     */
    @Inject(method = "open(Ljava/io/File;)V", at = @At("HEAD"), cancellable = true)
    public void openFile(File file, CallbackInfo ci) {
        CompletableFuture.runAsync(() -> {
            try {
                this.open(file.toURI().toURL());
            } catch (MalformedURLException e) {
                System.err.println("Couldn't open file '" + file + "'");
                e.printStackTrace();
            }
        });

        ci.cancel();
    }
}
