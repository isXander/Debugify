package cc.woverflow.debugify.mixins.client.mc121772;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mouse.class)
public class MouseMixin {
    /**
     * adapted from https://github.com/nelson2tm/shift-scroll-fix
     * under MIT license
     */
    @ModifyVariable(method = "onMouseScroll", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private double scrollFix(double vertical1, long window, double horizontal, double vertical2) {
        return vertical1 == 0 ? horizontal : vertical1;
    }
}
