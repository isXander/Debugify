package dev.isxander.debugify.client.mixins.basic.mc267469;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-267469", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "GUI List Entry highlight border not always aligned properly")
@Mixin(AbstractSelectionList.class)
public abstract class AbstractSelectionListMixin {
    @Shadow
    public abstract int getRowLeft();

    @Shadow
    public abstract int getRowRight();

    @Expression("? = @(? + (? - ?) / 2)")
    @ModifyExpressionValue(method = "renderSelection", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int modifyLeft(int i) {
        return getRowLeft() - 2;
    }

    @Expression("? = @(? + (? + ?) / 2)")
    @ModifyExpressionValue(method = "renderSelection", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int modifyRight(int j) {
        return getRowRight() - 2;
    }
}
