package dev.isxander.debugify.client.mixins.basic.mc201723;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Taken from Thorium
 * https://github.com/PotassiumMC/thorium
 * under LGPLv3 license
 *
 * @author NoahvdAa
 */
//@BugFix(id = "MC-201723", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Statistics sprites don't look pressed when clicked")
//@Mixin(StatsScreen.ItemStatisticsList.class)
//public class StatsScreenMixin extends ObjectSelectionList {
//    @Shadow
//    protected int headerPressed;
//
//    public StatsScreenMixin(Minecraft minecraft, int i, int j, int k, int l) {
//        super(minecraft, i, j, k, l);
//    }
//
//    @WrapOperation(method = "renderHeader", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHandler;isLeftPressed()Z"))
//    private boolean forceLeftClick(MouseHandler instance, Operation<Boolean> original) {
//        return true;
//    }
//
//    @Override
//    public boolean mouseReleased(double mouseX, double mouseY, int button) {
//        if (button == 0) {
//            this.headerPressed = -1;
//        }
//
//        return super.mouseReleased(mouseX, mouseY, button);
//    }
//}
