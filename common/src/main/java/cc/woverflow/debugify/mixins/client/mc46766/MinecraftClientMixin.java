package cc.woverflow.debugify.mixins.client.mc46766;

import cc.woverflow.debugify.fixes.BugFix;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@BugFix(id = "MC-46766", env = BugFix.Env.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean bl, CallbackInfo ci) {
        if (interactionManager.getCurrentGameMode() == GameMode.SPECTATOR)
            ci.cancel();
    }
}
