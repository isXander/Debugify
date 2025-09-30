package dev.isxander.debugify.client.mixins.basic.mc127970;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-127970", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Using Riptide on a trident with an item in your off-hand causes visual glitch with said item")
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @ModifyExpressionValue(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isAutoSpinAttack()Z"))
    private boolean isUsingRiptideHand(boolean original, @Local(argsOnly = true) ItemStack item) {
        return original && item.is(Items.TRIDENT);
    }
}
