package dev.isxander.debugify.client.mixins.basic.mc46503;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-46503", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "You can retain a mob's shader in spectator mode by running /kill")
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleRespawn", at = @At("TAIL"))
    private void removeEntityEffect(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        Minecraft.getInstance().gameRenderer.clearPostEffect();
    }
}
