package dev.isxander.debugify.client.helpers.mc237493;

import net.minecraft.client.option.ParticlesMode;
import net.minecraft.util.TranslatableOption;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Comparator;

public enum DebugifyTelemetry implements TranslatableOption {
    OFF(0, "options.telemetry.state.none"),
    MINIMAL(1, "options.telemetry.state.minimal"),
    ALL(2, "options.telemetry.state.all");

    private static final DebugifyTelemetry[] VALUES = Arrays.stream(values())
            .sorted(Comparator.comparingInt(DebugifyTelemetry::getId))
            .toArray(DebugifyTelemetry[]::new);

    private final int id;
    private final String translationKey;

    DebugifyTelemetry(int id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }

    public static DebugifyTelemetry byId(int id) {
        return VALUES[MathHelper.floorMod(id, VALUES.length)];
    }
}
