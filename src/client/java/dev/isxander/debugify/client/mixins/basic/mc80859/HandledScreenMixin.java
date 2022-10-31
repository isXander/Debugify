package dev.isxander.debugify.client.mixins.basic.mc80859;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@BugFix(id = "MC-80859", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    /**
     * If slots is size 1 then the inner method would run anyway and return so this just ignores the outer
     * statement and lets it continue to the rendering therefore fixing the bug.
     */
    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
    private boolean onQuickCraftCheck(Set<Slot> cursorDragSlots, Object slot) {
        if (cursorDragSlots.size() == 1) return false;
        //noinspection SuspiciousMethodCalls
        return cursorDragSlots.contains(slot);
    }
}
