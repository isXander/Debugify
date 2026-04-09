package dev.isxander.debugify.client.mixins.basic.mc237493;

import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.Tooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Arrays;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry", description = "Telemetry cannot be disabled")
@Mixin(Options.class)
public abstract class OptionsMixin implements DebugifyTelemetryAccessor {
    @Shadow public abstract OptionInstance<Boolean> telemetryOptInExtra();

    @Unique
    private final OptionInstance<DebugifyTelemetry> debugifyTelemetry = new OptionInstance<>(
            "options.telemetry.button",
            value -> Tooltip.create(value.tooltip()),
            (component, value) -> value.caption(),
            new OptionInstance.Enum<>(Arrays.asList(DebugifyTelemetry.values()), DebugifyTelemetry.LEGACY_CODEC),
            DebugifyTelemetry.OFF,
            value -> telemetryOptInExtra().set(value == DebugifyTelemetry.ALL)
    );

    @Override
    public OptionInstance<DebugifyTelemetry> getTelemetryOption() {
        return debugifyTelemetry;
    }

    @Inject(method = "processOptions", at = @At("RETURN"))
    private void shouldAcceptVanillaTelemetry(Options.FieldAccess access, CallbackInfo ci) {
        access.process("debugifyTelemetry", getTelemetryOption());
    }
}
