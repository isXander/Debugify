package dev.isxander.debugify.mixins.basic.client.mc210318;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@BugFix(id = "MC-210318", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = "thorium")
@Mixin(BookEditScreen.class)
public class BookEditScreenMixin {
    @ModifyConstant(method = "method_27593", constant = @Constant(intValue = 16))
    private static int modifyMaxLength(int original) {
        // < 17 == <= 16
        return 17;
    }
}
