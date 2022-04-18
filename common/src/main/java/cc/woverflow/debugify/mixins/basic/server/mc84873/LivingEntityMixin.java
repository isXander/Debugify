package cc.woverflow.debugify.mixins.basic.server.mc84873;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-84873", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    /**
     * With deathTime >= 20, the entity never gets removed
     * because the check is deathTime == 20 not deathTime >= 20.
     * This mixin basically replaces that.
     */
    @ModifyExpressionValue(method = "updatePostDeath", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;deathTime:I", ordinal = 1, opcode = Opcodes.GETFIELD))
    private int modifyDeathTimeCheck(int deathTime) {
        return Math.min(deathTime, 20);
    }
}
