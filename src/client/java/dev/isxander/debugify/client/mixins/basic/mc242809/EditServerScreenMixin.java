package dev.isxander.debugify.client.mixins.basic.mc242809;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.EditServerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-242809", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "IP field in the multiplayer menu will not detect the IP if a space is put at the beginning/end of it")
@Mixin(EditServerScreen.class)
public class EditServerScreenMixin {
    @Definition(id = "ipEdit", field = "Lnet/minecraft/client/gui/screens/EditServerScreen;ipEdit:Lnet/minecraft/client/gui/components/EditBox;")
    @Definition(id = "getValue", method = "Lnet/minecraft/client/gui/components/EditBox;getValue()Ljava/lang/String;")
    @Expression("?.ipEdit.getValue()")
    @ModifyExpressionValue(method = "onAdd", at = @At("MIXINEXTRAS:EXPRESSION"))
    private String trimIp(String ip) {
        return ip.trim();
    }
}
