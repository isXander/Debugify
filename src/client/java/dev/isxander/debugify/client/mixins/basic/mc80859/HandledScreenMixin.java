package dev.isxander.debugify.client.mixins.basic.mc80859;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@BugFix(id = "MC-80859", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Shadow @Final protected Set<Slot> cursorDragSlots;

    /**
     * If slots is size 1 then the inner method would run anyway and return so this just ignores the outer
     * statement and lets it continue to the rendering therefore fixing the bug.
     */
    @ModifyExpressionValue(method = "drawSlot", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
    private boolean onQuickCraftCheck(boolean containsSlot) {
        if (cursorDragSlots.size() == 1)
            return false;
        return containsSlot;
    }
}
