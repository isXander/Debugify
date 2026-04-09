package dev.isxander.debugify.mixins.basic.mc272431;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-272431", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Ender Dragon incorrect vertical velocity causes erratic behavior")
@Mixin(EnderDragon.class)
public class EnderDragonMixin {
    @Definition(id = "yRotA", field = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;yRotA:F")
    @Expression("this.yRotA * @(0.01)")
    @ModifyExpressionValue(method = "aiStep", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float fixTargetPath(float original) {
        return 0.1f;
    }

    @Definition(id = "setDeltaMovement", method = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V")
    @Definition(id = "add", method = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;")
    @Definition(id = "ydd", local = @Local(type = double.class, name = "ydd"))
    @Expression("this.setDeltaMovement(?.add(0.0, ydd * @(0.01), 0.0))")
    @ModifyExpressionValue(method = "aiStep", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double fixVerticalVelocity(double value) {
        return 0.1;
    }
}
