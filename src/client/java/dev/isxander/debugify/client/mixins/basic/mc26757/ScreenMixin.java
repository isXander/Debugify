package dev.isxander.debugify.client.mixins.basic.mc26757;

import dev.isxander.debugify.client.fixes.mc26757.TooltipWrapper;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@BugFix(id = "MC-26757", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "tooltipfix")
@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow public int width;
    @Shadow public int height;

    @Shadow protected TextRenderer textRenderer;

    @Unique
    private int debugify$modifiedX;

    @Unique
    private int debugify$modifiedY;

    @Redirect(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V"))
    private void wrapText(Screen instance, MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, MatrixStack dontuse, Text text) {
        instance.renderOrderedTooltip(matrices, TooltipWrapper.wrapTooltipLines(instance, textRenderer, Collections.singletonList(text)), x, y);
    }

    @Redirect(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V"))
    private void wrapTextList(Screen instance, MatrixStack matrices, List<? extends OrderedText> iHateOrderedText, int x, int y, MatrixStack dontuse, List<Text> lines) {
        instance.renderOrderedTooltip(matrices, TooltipWrapper.wrapTooltipLines(instance, textRenderer, lines), x, y);
    }

    @Redirect(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;Ljava/util/Optional;II)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;", ordinal = 0))
    private Stream<OrderedText> wrapTextListWidthData(Stream<Text> instance, Function<Text, OrderedText> function) {
        return TooltipWrapper.wrapTooltipLines((Screen) (Object) this, textRenderer, instance.toList()).stream();
    }

    @Inject(method = "renderTooltipFromComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void centerTooltip(MatrixStack matrices, List<TooltipComponent> components, int mouseX, int mouseY, CallbackInfo ci, int width, int height, int x, int y) {
        doCenterTooltip(mouseX, mouseY, width, height, x, y);
    }

    private void doCenterTooltip(int mouseX, int mouseY, int width, int height, int x, int y) {
        debugify$modifiedX = x;
        debugify$modifiedY = y;

        if (x < 4) {
            debugify$modifiedX = MathHelper.clamp(mouseX - width / 2, 6, this.width - width - 6);
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
        } else if (y + height > this.height + 2) {
            debugify$modifiedY = Math.max(this.height - height - 4, 4);
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
