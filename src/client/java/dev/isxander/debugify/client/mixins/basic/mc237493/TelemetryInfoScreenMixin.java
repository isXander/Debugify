package dev.isxander.debugify.client.mixins.basic.mc237493;

import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.telemetry.TelemetryEventWidget;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry")
@Mixin(TelemetryInfoScreen.class)
public class TelemetryInfoScreenMixin {
    @Shadow @Final private Options options;


    @Shadow private TelemetryEventWidget telemetryEventWidget;

    /**
     * @author
     * @reason
     */
    @Overwrite
    private AbstractWidget createTelemetryCheckbox() {
        return ((DebugifyTelemetryAccessor) options).getTelemetryOption().createButton(options, 0, 0, 308, state -> telemetryEventWidget.onOptInChanged(state == DebugifyTelemetry.ALL));
    }
}
