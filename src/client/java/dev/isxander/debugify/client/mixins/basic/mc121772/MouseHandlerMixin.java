package dev.isxander.debugify.client.mixins.basic.mc121772;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.fixes.OS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@BugFix(id = "MC-121772", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = { "mcmouser", "shift-scroll-fix" }, os = OS.MAC)
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    /**
     * Taken from <a href="https://github.com/nelson2tm/shift-scroll-fix">Shift-Scroll Fix</a>
     * under MIT license
     * <br>
     * Adapted to be able to be toggled off at ease in a user-friendly GUI
     *
     * @author nelson2tm
     */
    @ModifyVariable(method = "onScroll", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private double scrollFix(double vertical1, long window, double horizontal, double vertical2) {
        return vertical1 == 0 && Minecraft.ON_OSX ? horizontal : vertical1;
    }
}
