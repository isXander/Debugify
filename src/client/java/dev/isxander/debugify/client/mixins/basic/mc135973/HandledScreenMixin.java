package dev.isxander.debugify.client.mixins.basic.mc135973;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@BugFix(id = "MC-135973", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "ctrl-q")
@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void enableRepeatEvents(CallbackInfo ci) {
        this.client.keyboard.setRepeatEvents(true);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void disableRepeatEvents(CallbackInfo ci) {
        this.client.keyboard.setRepeatEvents(false);
    }
}
