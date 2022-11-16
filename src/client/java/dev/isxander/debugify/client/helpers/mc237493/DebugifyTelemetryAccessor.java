package dev.isxander.debugify.client.helpers.mc237493;

import net.minecraft.client.option.SimpleOption;

public interface DebugifyTelemetryAccessor {
    SimpleOption<DebugifyTelemetry> getTelemetryOption();
}
