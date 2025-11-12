package dev.isxander.debugify.client.mixins.basic.mc251068;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-251068", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "If you delete your only world, then you are no longer automatically thrown into the menu of creating a new world")
@Mixin(WorldSelectionList.class)
public abstract class WorldSelectionListMixin extends ObjectSelectionList<WorldSelectionList.Entry> {

    public WorldSelectionListMixin(Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    @WrapWithCondition(method = "returnToScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private boolean dontShowEmptyWorldList(Minecraft instance, Screen screen) {
        return (this.minecraft.screen instanceof ProgressScreen || this.minecraft.screen instanceof ConfirmScreen);
    }
}
