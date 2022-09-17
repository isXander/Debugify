package dev.isxander.debugify.client.mixins.basic.mc55347;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-55347", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "title-fix")
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow public abstract void clearTitle();

    @Shadow public abstract void setDefaultTitleFade();

    @Inject(method = "clear", at = @At("RETURN"))
    private void clearTitle(CallbackInfo ci) {
        this.clearTitle();
        this.setDefaultTitleFade();
    }
}
