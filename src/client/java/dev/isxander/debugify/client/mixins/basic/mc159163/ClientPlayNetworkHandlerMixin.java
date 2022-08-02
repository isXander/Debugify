package dev.isxander.debugify.client.mixins.basic.mc159163;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@BugFix(id = "MC-159163", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {
    @Final @Shadow private MinecraftClient client;

    @Inject(method = "onEntityTrackerUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;writeUpdatedEntries(Ljava/util/List;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void removeLocalEntityPose(EntityTrackerUpdateS2CPacket packet, CallbackInfo info, Entity entity) {
        if (entity.equals(client.player))
            packet.getTrackedValues().removeIf(p -> p.getData().getType().equals(TrackedDataHandlerRegistry.ENTITY_POSE));
    }
}
