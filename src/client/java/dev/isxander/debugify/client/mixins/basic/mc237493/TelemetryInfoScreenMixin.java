package dev.isxander.debugify.client.mixins.basic.mc237493;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.telemetry.TelemetryEventWidget;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry", description = "Telemetry cannot be disabled")
@Mixin(TelemetryInfoScreen.class)
public abstract class TelemetryInfoScreenMixin extends Screen {
    protected TelemetryInfoScreenMixin(Component title) {
        super(title);
    }

    @Shadow @Final private Options options;

    @Shadow private TelemetryEventWidget telemetryEventWidget;

    @Unique private AbstractWidget cycleButton;

    @Definition(id = "addChild", method = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;")
    @Definition(id = "builder", method = "Lnet/minecraft/client/gui/components/Checkbox;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;)Lnet/minecraft/client/gui/components/Checkbox$Builder;")
    @Definition(id = "CHECKBOX_OPT_IN", field = "Lnet/minecraft/client/gui/screens/telemetry/TelemetryInfoScreen;CHECKBOX_OPT_IN:Lnet/minecraft/network/chat/Component;")
    @Expression("?.addChild(builder(CHECKBOX_OPT_IN, ?).?(?).?(?.?.?()).?(?).?())")
    @WrapOperation(method = "init", at = @At("MIXINEXTRAS:EXPRESSION"))
    private LayoutElement createTelemetryCheckbox(LinearLayout instance, LayoutElement child, Operation<LayoutElement> original) {
        this.cycleButton = instance.addChild(((DebugifyTelemetryAccessor) options).getTelemetryOption().createButton(options, 0, 0, 308, state -> telemetryEventWidget.onOptInChanged(state == DebugifyTelemetry.ALL)));
        return null;
    }

    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void repositionCycleButton(CallbackInfo ci) {
        if (this.cycleButton != null) {
            this.cycleButton.setWidth(308);
        }
    }
}
