package dev.isxander.debugify.mixins.basic.mc84873;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-84873", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    /**
     * With deathTime being > 20, the entity never gets removed
     * because the check is deathTime == 20 not deathTime >= 20.
     * This mixin basically replaces that.
     */
    @ModifyExpressionValue(method = "updatePostDeath", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;deathTime:I", ordinal = 1, opcode = Opcodes.GETFIELD))
    private int modifyDeathTimeCheck(int deathTime) {
        return Math.min(deathTime, 20);
    }
}
