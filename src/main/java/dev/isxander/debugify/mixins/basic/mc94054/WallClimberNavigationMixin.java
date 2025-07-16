package dev.isxander.debugify.mixins.basic.mc94054;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-94054", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Cave spiders spin around when walking")
@Mixin(WallClimberNavigation.class)
public class WallClimberNavigationMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getBbWidth()F"))
    private float fixSpiderSpinning(Mob instance, Operation<Float> original) {
        return Math.max(original.call(instance), 1.0F);
    }
}
