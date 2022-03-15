package dev.isxander.communitypatches.mixins.mc165381;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    @Final
    protected MinecraftClient client;

    @Inject(method = "dropSelectedItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropSelectedItem(Z)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
    private void onDropItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        client.interactionManager.cancelBlockBreaking();
    }
}
