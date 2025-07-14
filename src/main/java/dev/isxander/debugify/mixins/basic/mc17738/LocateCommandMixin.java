package dev.isxander.debugify.mixins.basic.mc17738;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.server.commands.LocateCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-177831", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Game does not count the distance properly if you locate a structure from more than 46340 blocks away")
@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    @WrapOperation(method = "dist", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sqrt(F)F"))
    private static float distanceHypot(float f, Operation<Float> original, int pos1x, int pos1z, int pos2x, int pos2z) {
        return (float) Math.hypot(pos2x - pos1x, pos2z - pos1z);
    }
}
