package cc.woverflow.debugify.fabric.mixins.server.mc7569;

import net.minecraft.server.rcon.RconCommandOutput;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(RconCommandOutput.class)
public class RconCommandOutputMixin {
    @Shadow @Final private StringBuffer buffer;

    @Inject(method = "sendSystemMessage", at = @At("RETURN"))
    private void appendNewline(Text message, UUID sender, CallbackInfo ci) {
        buffer.append(System.lineSeparator());
    }
}
