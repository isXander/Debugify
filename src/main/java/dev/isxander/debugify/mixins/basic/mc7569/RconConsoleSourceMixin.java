package dev.isxander.debugify.mixins.basic.mc7569;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.rcon.RconConsoleSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-7569", category = FixCategory.BASIC, env = BugFix.Env.SERVER)
@Mixin(RconConsoleSource.class)
public class RconConsoleSourceMixin {
    @Shadow @Final private StringBuffer buffer;

    @Inject(method = "sendSystemMessage", at = @At("RETURN"))
    private void appendNewline(Component message, CallbackInfo ci) {
        buffer.append(System.lineSeparator());
    }
}
