package dev.isxander.debugify.mixins.basic.mc94054;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-94054", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Cave spiders spin around when walking")
@Mixin(WallClimberNavigation.class)
public class WallClimberNavigationMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getBbWidth()F"))
    private float fixSpiderSpinning(float bbWidth) {
        return Math.max(bbWidth, 1.0F);
    }
}
