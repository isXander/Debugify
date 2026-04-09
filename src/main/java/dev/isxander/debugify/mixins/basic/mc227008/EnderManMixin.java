package dev.isxander.debugify.mixins.basic.mc227008;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-227008", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Enderman constantly tries to teleport when in a boat or a minecart under daylight")
@Mixin(EnderMan.class)
public class EnderManMixin {
    // Target getLightLevelDependentMagicValue so we get an EnderMan instance without needing to cast `this`. As long as this returns <= 0.5F the enderman won't teleport
    @WrapOperation(
            method = "customServerAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/EnderMan;getLightLevelDependentMagicValue()F"
            )
    )
    private float checkRiding(EnderMan instance, Operation<Float> original) {
        return instance.isPassenger() ? 0F : original.call(instance);
    }
}
