package dev.isxander.debugify.client.mixins.basic.mc237493;

import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetry;
import dev.isxander.debugify.client.helpers.mc237493.DebugifyTelemetryAccessor;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.class_7941;
import net.minecraft.class_7944;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@BugFix(id = "MC-237493", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "no-telemetry")
@Mixin(class_7944.class)
public class TelemetryOptionsScreenMixin {
    @Shadow @Final private GameOptions field_41374;


    @Shadow private class_7941 field_41375;

    /**
     * @author
     * @reason
     */
    @Overwrite
    private ClickableWidget method_47653() {
        return ((DebugifyTelemetryAccessor) field_41374).getTelemetryOption().method_47603(field_41374, 0, 0, 150, state -> field_41375.method_47638(state == DebugifyTelemetry.ALL));
    }
}
