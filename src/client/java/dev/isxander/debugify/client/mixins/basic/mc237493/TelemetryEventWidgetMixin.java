package dev.isxander.debugify.client.mixins.basic.mc237493;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.telemetry.TelemetryEventWidget;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(TelemetryEventWidget.class)
public class TelemetryEventWidgetMixin {
    @Shadow @Final private Font font;

    @ModifyExpressionValue(method = "buildContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/telemetry/TelemetryEventType;values()Ljava/util/List;"))
    private List<TelemetryEventType> modifyInUseTelemetry(List<TelemetryEventType> inUse) {
        if (((DebugifyTelemetryAccessor) Minecraft.getInstance().options).getTelemetryOption().get() == DebugifyTelemetry.OFF)
            return List.of();
        return inUse;
    }

    @Inject(method = "buildContent", at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addDebugifyContent(boolean bl, CallbackInfoReturnable<TelemetryEventWidget.Content> cir, TelemetryEventWidget.ContentBuilder contentBuilder) {
        if (((DebugifyTelemetryAccessor) Minecraft.getInstance().options).getTelemetryOption().get() == DebugifyTelemetry.OFF) {
            contentBuilder.addHeader(this.font, Component.translatable("debugify.mc_237493.header"));
            contentBuilder.addLine(this.font, Component.translatable("debugify.mc_237493.line", Component.translatable("options.telemetry.state.none")).withStyle(ChatFormatting.GRAY));
        }
    }
}
