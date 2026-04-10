package dev.isxander.debugify.client.mixins.basic.mc217716;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-217716", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "The green nausea overlay isn't removed when switching into spectator mode")
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final
    private Minecraft minecraft;

    // Naughty minecraft using references to player in `render`!!
    // This should be in extractOptions and we shouldn't be referencing player either here but
    // we have to because mojang gets the nausea blend factor here
    @Definition(id = "getEffectBlendFactor", method = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F")
    @Definition(id = "NAUSEA", field = "Lnet/minecraft/world/effect/MobEffects;NAUSEA:Lnet/minecraft/core/Holder;")
    @Expression("?.getEffectBlendFactor(NAUSEA, ?)")
    @ModifyExpressionValue(method = "renderLevel", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float shouldShowNauseaOverlay(float original) {
        return minecraft.player.isSpectator() ? 0.0f : original;
    }
}
