package dev.isxander.debugify.mixins.basic.mc243057;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Taken from a Paper patch attributed to Machine_Maker
 * <a href="https://github.com/PaperMC/Paper/blob/4f79e9eeca8549c72794f53e88c075c1eba46c58/paper-server/patches/sources/net/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity.java.patch#L276">patch</a>
 * <p>
 * Licensed under MIT
 * <a href="https://github.com/PaperMC/Paper/blob/main/LICENSE.md">license</a>
 */
@BugFix(id = "MC-243057", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Furnace recipes don't consider alternatives when first option of recipe is used for fuel")
@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Shadow
    protected NonNullList<ItemStack> items;

    @Shadow
    @Final
    protected static int SLOT_INPUT;

    @Shadow
    @Final
    protected static int SLOT_RESULT;

    @Inject(method = "fillStackedContents", at = @At("HEAD"))
    private void fixFuelStacks(StackedItemContents stackedItemContents, CallbackInfo ci) {
        stackedItemContents.accountStack(this.items.get(SLOT_INPUT));
        stackedItemContents.accountStack(this.items.get(SLOT_RESULT));
    }
}
