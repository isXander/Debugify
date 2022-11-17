package dev.isxander.debugify.client.helpers.mc237493;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;

public enum DebugifyTelemetry implements OptionEnum {
    OFF(0, "options.telemetry.state.none", "debugify.mc_237493.tooltip.off"),
    MINIMAL(1, "options.telemetry.state.minimal", "debugify.mc_237493.tooltip.minimal"),
    ALL(2, "options.telemetry.state.all", "debugify.mc_237493.tooltip.all");

    private static final DebugifyTelemetry[] VALUES = Arrays.stream(values())
            .sorted(Comparator.comparingInt(DebugifyTelemetry::getId))
            .toArray(DebugifyTelemetry[]::new);

    private final int id;
    private final String translationKey;
    private final String tooltipTranslationKey;

    DebugifyTelemetry(int id, String translationKey, String tooltipTranslationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tooltipTranslationKey = tooltipTranslationKey;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getKey() {
        return this.translationKey;
    }

    public String getTooltipKey() {
        return this.tooltipTranslationKey;
    }

    public Component getTooltipText() {
        return Component.translatable(getTooltipKey(), getCaption());
    }

    public static DebugifyTelemetry byId(int id) {
        return VALUES[Mth.positiveModulo(id, VALUES.length)];
    }
}
