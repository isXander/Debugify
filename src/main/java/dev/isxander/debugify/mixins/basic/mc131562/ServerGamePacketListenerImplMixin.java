package dev.isxander.debugify.mixins.basic.mc131562;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-131562", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Pressing the \"Done\" button in empty command block minecarts prints the message \"Command set: \" in chat")
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    /**
     * Add a condition to only send the success message if the command is not empty.
     * This is parity with the command block behaviour.
     */
    @Definition(id = "sendSystemMessage", method = "Lnet/minecraft/server/level/ServerPlayer;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V")
    @Expression("?.?.sendSystemMessage(?(?, ?))")
    @WrapWithCondition(method = "handleSetCommandMinecart", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean checkEmptyCommandBeforeMessage(ServerPlayer instance, Component message, @Local(argsOnly = true) ServerboundSetCommandMinecartPacket packet) {
        return !StringUtil.isNullOrEmpty(packet.getCommand());
    }
}
