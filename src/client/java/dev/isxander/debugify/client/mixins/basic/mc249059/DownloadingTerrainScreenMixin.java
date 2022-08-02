package dev.isxander.debugify.client.mixins.basic.mc249059;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Taken from
 * https://github.com/kennytv/kennytvs-epic-force-close-loading-screen-mod-for-fabric
 * under MIT License
 */
@BugFix(id = "MC-249059", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = "forcecloseloadingscreen")
@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {
    @Shadow private boolean closeOnNextTick;

    /**
     * for some reason mojang waits 2 seconds even after
     * the terrain is ready (guessing to avoid lag of spawning entities?)
     */
    @Inject(method = "setReady", at = @At("HEAD"))
    private void onReady(CallbackInfo ci) {
        closeOnNextTick = true;
    }
}
