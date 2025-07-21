package dev.isxander.debugify.client.mixins.basic.mc251068;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-251068", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "If you delete your only world, then you are no longer automatically thrown into the menu of creating a new world")
@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldSelectionListMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapWithCondition(method = "method_20170", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", ordinal = 1))
    private boolean dontShowEmptyWorldList(Minecraft instance, Screen screen) {
        return (this.minecraft.screen instanceof ProgressScreen || this.minecraft.screen instanceof ConfirmScreen);
    }
}
