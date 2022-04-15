package cc.woverflow.debugify.mixins.client.mc145929;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Taken from TieFix
 * https://github.com/j-tai/TieFix
 * under LGPLv3 license
 *
 * Adapted to work in a multi-loader environment
 *
 * @author j-tai
 */
@Restriction(conflicts = {
        @Condition("tiefix"),
        @Condition("shadowedactionbar")
})
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    private int drawActionBarShadow(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        return textRenderer.drawWithShadow(matrices, text, x, y, color);
    }
}
