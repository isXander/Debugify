package dev.isxander.communitypatches.mixins.mc122627;

import net.minecraft.client.gui.screen.CommandSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CommandSuggestor.SuggestionWindow.class)
public class CommandSuggestorMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawableHelper;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 4), index = 3)
    private int getX2(int x) {
        return x + 1;
    }
}
