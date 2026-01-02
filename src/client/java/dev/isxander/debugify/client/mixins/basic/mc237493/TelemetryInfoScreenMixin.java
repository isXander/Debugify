package dev.isxander.debugify.client.mixins.basic.mc237493;

import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Options;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.telemetry.TelemetryEventWidget;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry", description = "Telemetry cannot be disabled")
@Mixin(TelemetryInfoScreen.class)
public class TelemetryInfoScreenMixin {
    @Shadow @Final private Options options;


    @Shadow private TelemetryEventWidget telemetryEventWidget;

    /**
     * @author
     * @reason
     */
    @Redirect(method = "init", at = @At(ordinal = 5, value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
    private LayoutElement createTelemetryCheckbox(LinearLayout layout, LayoutElement child) {
        return layout.addChild(((DebugifyTelemetryAccessor) options).getTelemetryOption().createButton(options, 0, 0, 308, state -> telemetryEventWidget.onOptInChanged(state == DebugifyTelemetry.ALL)));
    }
}
