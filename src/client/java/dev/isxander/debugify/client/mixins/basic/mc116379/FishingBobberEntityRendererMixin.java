package dev.isxander.debugify.client.mixins.basic.mc116379;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@BugFix(id = "MC-116379", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 2)
    private float modifyHandSwingProgress(float handSwingProgress, FishingBobberEntity bobber) {
        PlayerEntity player = bobber.getPlayerOwner();
        int j = player.getMainArm() == Arm.RIGHT ? 1 : -1;
        int j2 = j;
        ItemStack itemStack = player.getMainHandStack();
        if (!itemStack.isOf(Items.FISHING_ROD)) {
            j = -j;
        }

        return j == j2 ? handSwingProgress : 0;
    }
}
