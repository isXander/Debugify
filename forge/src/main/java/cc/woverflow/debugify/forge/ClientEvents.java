package cc.woverflow.debugify.forge;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.client.DebugifyClient;
import cc.woverflow.debugify.client.utils.ConfigGuiHelper;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        DebugifyClient.onInitializeClient();

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory(
                        (mc, screen) -> ConfigGuiHelper.createConfigGui(Debugify.config, screen)
                )
        );
    }
}
