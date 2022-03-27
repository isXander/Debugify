package cc.woverflow.debugify.forge;

import cc.woverflow.debugify.Debugify;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("debugify")
public class DebugifyMod {
    public DebugifyMod() {
        Debugify.onInitialize();

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory(
                        (mc, screen) -> Debugify.config.createConfigGui(screen)
                )
        );
    }
}
