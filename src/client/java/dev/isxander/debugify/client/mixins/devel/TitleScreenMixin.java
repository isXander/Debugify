package dev.isxander.debugify.client.mixins.devel;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.gui.ConfigGuiHelper;
import dev.isxander.debugify.utils.DevelOnly;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@DevelOnly
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addSettingsButton(CallbackInfo ci) {
        addRenderableWidget(
                Button.builder(Component.literal("Debugify"), button -> minecraft.setScreen(ConfigGuiHelper.createConfigGui(Debugify.CONFIG, minecraft.screen)))
                        .pos(0, 0)
                        .width(50)
                        .build()
        );
    }
}
