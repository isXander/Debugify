package dev.isxander.debugify.client.mixins.basic.mc121772;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@BugFix(id = "MC-121772", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = { "mcmouser", "shift-scroll-fix" })
@Mixin(Mouse.class)
public class MouseMixin {
    /**
     * Taken from <a href="https://github.com/nelson2tm/shift-scroll-fix">Shift-Scroll Fix</a>
     * under MIT license
     *
     * Adapted to be able to be toggled off at ease in a user-friendly GUI
     *
     * @author nelson2tm
     */
    @ModifyVariable(method = "onMouseScroll", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private double scrollFix(double vertical1, long window, double horizontal, double vertical2) {
        return vertical1 == 0 && MinecraftClient.IS_SYSTEM_MAC ? horizontal : vertical1;
    }
}
