package dev.isxander.debugify.forge;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.DebugifyClient;
import dev.isxander.debugify.client.utils.ConfigGuiHelper;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        DebugifyClient.onInitializeClient();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, screen) -> ConfigGuiHelper.createConfigGui(Debugify.config, screen)
                )
        );
    }
}
