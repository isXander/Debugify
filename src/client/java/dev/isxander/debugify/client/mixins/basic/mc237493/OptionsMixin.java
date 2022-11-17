package dev.isxander.debugify.client.mixins.basic.mc237493;

import com.mojang.serialization.Codec;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Arrays;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry")
@Mixin(Options.class)
public abstract class OptionsMixin implements DebugifyTelemetryAccessor {
    @Shadow public abstract OptionInstance<Boolean> telemetryOptInExtra();

    @Unique
    private final OptionInstance<DebugifyTelemetry> debugifyTelemetry = new OptionInstance<>(
            "options.telemetry.button",
            value -> Tooltip.create(value.getTooltipText()),
            OptionInstance.forOptionEnum(),
            new OptionInstance.Enum<>(Arrays.asList(DebugifyTelemetry.values()), Codec.INT.xmap(DebugifyTelemetry::byId, DebugifyTelemetry::getId)),
            DebugifyTelemetry.OFF,
            value -> telemetryOptInExtra().set(value == DebugifyTelemetry.ALL)
    );

    @Override
    public OptionInstance<DebugifyTelemetry> getTelemetryOption() {
        return debugifyTelemetry;
    }

    @Inject(method = "processOptions", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=telemetryOptInExtra")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options$FieldAccess;process(Ljava/lang/String;Lnet/minecraft/client/OptionInstance;)V", ordinal = 0))
    private void shouldAcceptVanillaTelemetry(Options.FieldAccess visitor, CallbackInfo ci) {
        visitor.process("debugifyTelemetry", getTelemetryOption());
    }
}
