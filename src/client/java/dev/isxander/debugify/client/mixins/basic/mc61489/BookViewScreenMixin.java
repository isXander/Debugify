package dev.isxander.debugify.client.mixins.basic.mc61489;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Compared to the original patch, we use / 3 here since it seems to line up better. Else, in a windowed screen the Done button appears at the very bottom / slightly off-screen.
@BugFix(id = "MC-61489", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, enabled = false, modConflicts = {"fixbookgui", "stendhal", "scribble"}, description = "Book GUI is not vertically centered")
@Mixin(BookViewScreen.class)
public class BookViewScreenMixin extends Screen {
    @Shadow
    @Final
    protected static int IMAGE_HEIGHT;

    protected BookViewScreenMixin(Component component) {
        super(component);
    }

    @ModifyArg(method = "createMenuControls", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"), index = 1)
    private int modifyMenuControlsYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "createPageControlButtons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/PageButton;<init>(IIZLnet/minecraft/client/gui/components/Button$OnPress;Z)V"), index = 1)
    private int modifyPageControlsYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V"), index = 3)
    private int modifyPageMessageYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V"), index = 3)
    private int modifyPageComponentsYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"), index = 3)
    private int translateRenderBackground(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @WrapMethod(method = "getClickedComponentStyleAt")
    private Style modifyClickedComponentStyleAtYPos(double x, double y, Operation<Style> original) {
        return original.call(x, y - (this.height - IMAGE_HEIGHT) / 3);
    }
}
