package cc.woverflow.debugify.forge;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.config.ConfigGuiHelper;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("debugify")
public class DebugifyMod {
    public DebugifyMod() {
        Debugify.onInitialize();

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory(
                        (mc, screen) -> ConfigGuiHelper.createConfigGui(Debugify.config, screen)
                )
        );
    }
}
