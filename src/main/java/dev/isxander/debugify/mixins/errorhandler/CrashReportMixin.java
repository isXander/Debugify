package dev.isxander.debugify.mixins.errorhandler;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import dev.isxander.debugify.error.CrashReportInjector;
import net.minecraft.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Definition(id = "systemReport", field = "Lnet/minecraft/CrashReport;systemReport:Lnet/minecraft/SystemReport;")
    @Definition(id = "appendToCrashReportString", method = "Lnet/minecraft/SystemReport;appendToCrashReportString(Ljava/lang/StringBuilder;)V")
    @Expression("this.systemReport.appendToCrashReportString(?)")
    @Inject(method = "getDetails(Ljava/lang/StringBuilder;)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void appendCrashReport(StringBuilder sb, CallbackInfo ci) {
        CrashReportInjector.addDetailsToCrashReport(sb);
    }
}
