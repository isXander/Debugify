package cc.woverflow.debugify.mixins.client.mc249059;

import cc.woverflow.debugify.fixes.BugFix;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-249059", env = BugFix.Env.CLIENT, fabricConflicts = "forcecloseloadingscreen")
@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {
    @Shadow private boolean oneTickSkipped;

    /**
     * for some reason mojang waits 2 seconds even after
     * the terrain is ready (guessing to avoid lag of spawning entities?)
     */
    @Inject(method = "setLoadingPacketsReceived", at = @At("HEAD"))
    private void onReady(CallbackInfo ci) {
        oneTickSkipped = true;
    }
}
