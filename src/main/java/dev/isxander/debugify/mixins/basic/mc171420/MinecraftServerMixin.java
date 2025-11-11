package dev.isxander.debugify.mixins.basic.mc171420;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-171420", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "OP players get kicked for not being on the whitelist (enforce = on)")
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    public abstract PlayerList getPlayerList();

    @WrapWithCondition(method = "kickUnlistedPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V"))
    private boolean isOpCheck(ServerGamePacketListenerImpl instance, Component component, @Local ServerPlayer serverPlayer) {
        return !this.getPlayerList().isOp(serverPlayer.getGameProfile());
    }
}
