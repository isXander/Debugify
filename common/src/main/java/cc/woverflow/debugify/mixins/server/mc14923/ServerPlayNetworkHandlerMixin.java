package cc.woverflow.debugify.mixins.server.mc14923;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow @Final private MinecraftServer server;

    @Shadow public ServerPlayerEntity player;

    /**
     * Currently, Minecraft checks if a player is OP to know to kick for spamming,
     * but in singleplayer, with cheats off, you are not opped.
     * This mixin also checks if the user is the host.
     */
    @Inject(method = "handleMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;disconnect(Lnet/minecraft/text/Text;)V"), cancellable = true)
    public void onMessage(TextStream.Message message, CallbackInfo ci){
        if (this.server.isHost(this.player.getGameProfile()))
            ci.cancel();
    }
}
