package dev.isxander.debugify.client.helpers.mc237493;

import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntFunction;

import net.minecraft.network.chat.Component;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

public enum DebugifyTelemetry {
    OFF(0, "options.telemetry.state.none", "debugify.mc_237493.tooltip.off"),
    MINIMAL(1, "options.telemetry.state.minimal", "debugify.mc_237493.tooltip.minimal"),
    ALL(2, "options.telemetry.state.all", "debugify.mc_237493.tooltip.all");

    private static final IntFunction<DebugifyTelemetry> BY_ID = ByIdMap.continuous(value -> value.id, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final Codec<DebugifyTelemetry> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, value -> value.id);
    private final int id;
    private final Component caption;
    private final Component tooltip;

    DebugifyTelemetry(int id, final String key, final String translationKey) {
        this.id = id;
        this.caption = Component.translatable(key);
        this.tooltip = Component.translatable(translationKey);
    }

    public Component caption() {
        return this.caption;
    }

    public Component tooltip() {
        return this.tooltip;
    }
}
