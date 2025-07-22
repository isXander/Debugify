package dev.isxander.debugify.mixins.basic.mc272431;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-272431", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Ender Dragon incorrect vertical velocity causes erratic behavior")
@Mixin(EnderDragon.class)
public class EnderDragonMixin {
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "CONSTANT", args = "doubleValue=0.01"))
    private double fixTargetPath(double value) {
        return 0.1;
    }
}
