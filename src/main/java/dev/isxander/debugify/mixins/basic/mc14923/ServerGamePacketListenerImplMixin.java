package dev.isxander.debugify.mixins.basic.mc14923;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-14923", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow @Final private MinecraftServer server;

    @Shadow public ServerPlayer player;

    /**
     * Currently, Minecraft checks if a player is OP to know to kick for spamming,
     * but in singleplayer, with cheats off, you are not op-ed.
     * This mixin also checks if the user is the host.
     */
    @ModifyExpressionValue(method = "detectRateSpam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"))
    public boolean onMessage(boolean operator){
        return operator || this.server.isSingleplayerOwner(this.player.getGameProfile());
    }
}
