package dev.isxander.debugify.client.mixins.basic.mc122627;

import com.llamalad7.mixinextras.expression.Definition;import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.CommandSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@BugFix(id = "MC-122627", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Tab suggestion box has missing padding on right side")
@Mixin(CommandSuggestions.class)
public class CommandSuggestionsMixin {
    /**
     * Minecraft renders the text at +1 on the x-axis
     * but does not compensate and add 1 to the width of the box
     */
    @Definition(id = "fill", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
    @Definition(id = "commandUsagePosition", field = "Lnet/minecraft/client/gui/components/CommandSuggestions;commandUsagePosition:I")
    @Definition(id = "commandUsageWidth", field = "Lnet/minecraft/client/gui/components/CommandSuggestions;commandUsageWidth:I")
    @Expression("?.fill(?, ?, @(this.commandUsagePosition + this.commandUsageWidth + 1), ?, ?)")
    @ModifyExpressionValue(method = "extractUsage", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int modifyX2(int x) {
        return x + 1;
    }
}
