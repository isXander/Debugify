package dev.isxander.debugify.client.mixins.basic.mc217716;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-217716", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private boolean shouldShowNauseaOverlay(boolean hasNausea) {
        return hasNausea && !client.player.isSpectator();
    }
}
