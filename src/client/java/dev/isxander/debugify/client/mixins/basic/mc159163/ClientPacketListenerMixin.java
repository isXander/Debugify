package dev.isxander.debugify.client.mixins.basic.mc159163;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@BugFix(id = "MC-159163", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin implements ClientGamePacketListener {
    @Final @Shadow private Minecraft minecraft;

    @Inject(method = "handleSetEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;assignValues(Ljava/util/List;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void removeLocalEntityPose(ClientboundSetEntityDataPacket packet, CallbackInfo info, Entity entity) {
        if (entity.equals(minecraft.player))
            packet.packedItems().removeIf(p -> p.serializer().equals(EntityDataSerializers.POSE));
    }
}
