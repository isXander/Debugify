package dev.isxander.debugify.client.mixins.basic.mc251068;

import dev.isxander.debugify.client.helpers.mc251068.LastWorldDeleted;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-251068", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "If you delete your only world, then you are no longer automatically thrown into the menu of creating a new world")
@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListEntryMixin {
    @Shadow
    @Final
    private WorldSelectionList list;

    @Inject(method = "doDeleteWorld", at = @At("TAIL"))
    private void markLastWorldDeleted(CallbackInfo ci) {
        if (this.list.children().size() <= 1) {
            LastWorldDeleted.mark();
        }
    }
}
