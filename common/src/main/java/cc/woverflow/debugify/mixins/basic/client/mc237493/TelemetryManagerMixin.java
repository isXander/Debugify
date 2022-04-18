package cc.woverflow.debugify.mixins.basic.client.mc237493;

import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.FixCategory;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.util.telemetry.TelemetryManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, fabricConflicts = "tiefix")
@Mixin(TelemetryManager.class)
public class TelemetryManagerMixin {
    @ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;isDevelopment:Z", opcode = Opcodes.GETFIELD))
    private boolean shouldNotSendTelemetry(boolean isDevelopment) {
        return true;
    }
}
