package cc.woverflow.debugify.forge.mixins.client.mc215531;

import cc.woverflow.debugify.fixes.BugFix;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-215531", env = BugFix.Env.CLIENT)
@Mixin(ForgeIngameGui.class)
public class ForgeIngameGuiMixin extends InGameHud {
    public ForgeIngameGuiMixin(MinecraftClient client) {
        super(client);
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
