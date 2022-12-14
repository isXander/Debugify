package dev.isxander.debugify.client.mixins.basic.mc237493;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.minecraft.TelemetryEvent;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.TelemetryEventInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry")
@Mixin(TelemetryEventInstance.class)
public class TelemetryEventInstanceMixin {
    @ModifyReturnValue(method = "export", at = @At("RETURN"))
    private TelemetryEvent preventEvents(TelemetryEvent realEvent) {
        if (((DebugifyTelemetryAccessor) Minecraft.getInstance().options).getTelemetryOption().get() == DebugifyTelemetry.OFF) {
            return TelemetryEvent.EMPTY;
        }

        return realEvent;
    }
}
