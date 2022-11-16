package dev.isxander.debugify.client.mixins.basic.mc237493;

import com.mojang.serialization.Codec;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Arrays;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry")
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin implements DebugifyTelemetryAccessor {
    @Shadow public abstract SimpleOption<Boolean> method_47609();

    @Unique
    private final SimpleOption<DebugifyTelemetry> debugifyTelemetry = new SimpleOption<>(
            "options.telemetry.button",
            SimpleOption.emptyTooltip(), // TODO implement dynamic tooltip
            SimpleOption.enumValueText(),
            new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(DebugifyTelemetry.values()), Codec.INT.xmap(DebugifyTelemetry::byId, DebugifyTelemetry::getId)),
            DebugifyTelemetry.OFF,
            value -> method_47609().setValue(value == DebugifyTelemetry.ALL)
    );

    @Override
    public SimpleOption<DebugifyTelemetry> getTelemetryOption() {
        return debugifyTelemetry;
    }

    @Redirect(method = "accept", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=telemetryOptInExtra")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions$Visitor;accept(Ljava/lang/String;Lnet/minecraft/client/option/SimpleOption;)V", ordinal = 0))
    private void shouldAcceptVanillaTelemetry(GameOptions.Visitor visitor, String name, SimpleOption<?> option) {
        visitor.accept("debugifyTelemetry", getTelemetryOption());
    }
}
