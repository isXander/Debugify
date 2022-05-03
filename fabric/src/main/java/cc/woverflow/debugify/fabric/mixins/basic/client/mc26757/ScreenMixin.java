package cc.woverflow.debugify.fabric.mixins.basic.client.mc26757;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@BugFix(id = "MC-26757", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow public int height;
    @Unique
    private int debugify$modifiedX;

    @Unique
    private int debugify$modifiedY;

    @Inject(method = "renderTooltipFromComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void centerTooltip(MatrixStack matrices, List<TooltipComponent> components, int mouseX, int mouseY, CallbackInfo ci, int width, int height, int x, int y) {
        doCenterTooltip(mouseX, mouseY, width, height, x, y);
    }

    private void doCenterTooltip(int mouseX, int mouseY, int width, int height, int x, int y) {
        if (x < -2) {
            debugify$modifiedX = mouseX - width / 2;
            debugify$modifiedY = mouseY - height - 12;

            if (debugify$modifiedY < 6) {
                // find amount of obstruction to decide if it
                // is best to be above or below cursor
                var below = mouseY + 12;
                var belowObstruction = below + height - this.height;
                var aboveObstruction = -debugify$modifiedY;

                if (belowObstruction < aboveObstruction) {
                    debugify$modifiedY = below;
                }
            }
        } else {
            debugify$modifiedX = x;
            debugify$modifiedY = y;
        }
    }

    /**
     * cursed modifyvariable because you can't modify localcaptures
     */
    @ModifyVariable(method = "renderTooltipFromComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", ordinal = 0), ordinal = 4)
    private int modifyX(int x) {
        return debugify$modifiedX;
    }

    @ModifyVariable(method = "renderTooltipFromComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", ordinal = 0), ordinal = 5)
    private int modifyY(int y) {
        return debugify$modifiedY;
    }
}
