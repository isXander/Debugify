package dev.isxander.debugify.client.mixins.basic.mc227169;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-227169", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @ModifyExpressionValue(method = "getArmPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean shouldUseCrossbowPose(boolean crossbowCharged, AbstractClientPlayerEntity player, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        return crossbowCharged && (hand == Hand.MAIN_HAND || client.cameraEntity != player || !client.options.getPerspective().isFirstPerson());
    }
}
