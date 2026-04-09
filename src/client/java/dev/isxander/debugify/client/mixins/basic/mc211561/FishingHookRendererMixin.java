package dev.isxander.debugify.client.mixins.basic.mc211561;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-211561", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Fishing line appears in opposite hand when switching slots")
@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {
    @ModifyReturnValue(method = "getHoldingArm", at = @At("RETURN"))
    private static HumanoidArm fixHoldingArm(HumanoidArm original, Player owner) {
        return owner.getOffhandItem().getItem() instanceof FishingRodItem ? original : owner.getMainArm();
    }
}
