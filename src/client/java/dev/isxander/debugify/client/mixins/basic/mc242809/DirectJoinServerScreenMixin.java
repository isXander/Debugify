package dev.isxander.debugify.client.mixins.basic.mc242809;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-242809", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "IP field in the multiplayer menu will not detect the IP if a space is put at the beginning/end of it")
@Mixin(DirectJoinServerScreen.class)
public class DirectJoinServerScreenMixin {
    @WrapOperation(method = "onSelect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;getValue()Ljava/lang/String;"))
    private String trimIpSelect(EditBox instance, Operation<String> original) {
        return original.call(instance).trim();
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V"))
    private void trimIpInit(EditBox instance, String string, Operation<Void> original) {
        original.call(instance, string.trim());
    }
}
