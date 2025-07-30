package dev.isxander.debugify.mixins.basic.mc131562;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-131562", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Pressing the \"Done\" button in empty command block minecarts prints the message \"Command set:\" in chat")
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @WrapWithCondition(method = "handleSetCommandMinecart", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V"))
    private boolean checkEmptyCommand(ServerPlayer instance, Component message, ServerboundSetCommandMinecartPacket packet) {
        return !StringUtil.isNullOrEmpty(packet.getCommand());
    }
}
