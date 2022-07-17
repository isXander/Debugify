package dev.isxander.debugify.forge.mixins.basic.client.mc215531;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-215531", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ForgeGui.class)
public class ForgeGuiMixin extends InGameHud {
    public ForgeGuiMixin(MinecraftClient client, ItemRenderer itemRenderer) {
        super(client, itemRenderer);
    }

    /**
     * forge why do you think there is a need
     * to completely rewrite ingamehud i hate
     * you so so so so so so so much
     */
    @ModifyExpressionValue(method = "renderHelmet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean shouldRenderPumpkin(boolean firstPerson) {
        return firstPerson && !client.player.isSpectator();
    }
}
