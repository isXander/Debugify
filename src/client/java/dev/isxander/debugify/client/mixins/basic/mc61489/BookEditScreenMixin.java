package dev.isxander.debugify.client.mixins.basic.mc61489;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Compared to the original patch, we use / 3 here since it seems to line up better. Else, in a windowed screen the Done button appears at the very bottom / slightly off-screen.
@BugFix(id = "MC-61489", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "fixbookgui", description = "Book GUI is not vertically centered")
@Mixin(BookEditScreen.class)
public class BookEditScreenMixin extends Screen {
    @Shadow
    @Final
    public static int IMAGE_HEIGHT;

    protected BookEditScreenMixin(Component component) {
        super(component);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/MultiLineEditBox$Builder;setY(I)Lnet/minecraft/client/gui/components/MultiLineEditBox$Builder;"))
    private int modifyHeight(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/PageButton;<init>(IIZLnet/minecraft/client/gui/components/Button$OnPress;Z)V"), index = 1)
    private int modifyPageButtonsYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"), index = 1)
    private int modifyButtonsYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V"), index = 3)
    private int modifyStringYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }

    @ModifyArg(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"), index = 3)
    private int modifyBlitYPos(int original) {
        return original + (this.height - IMAGE_HEIGHT) / 3;
    }
}
