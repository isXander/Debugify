package dev.isxander.debugify.client.mixins.basic.mc59810;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.Mouse;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-59810", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, enabled = false, modConflicts = "mcmouser", os = OS.MAC)
@Mixin(Mouse.class)
public class MouseMixin {
    @ModifyExpressionValue(method = "onMouseButton", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;IS_SYSTEM_MAC:Z", opcode = Opcodes.GETFIELD))
    private boolean doRightClickEmulation(boolean isMac) {
        return false;
    }
}
