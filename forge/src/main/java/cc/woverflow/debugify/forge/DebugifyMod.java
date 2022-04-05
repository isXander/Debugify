package cc.woverflow.debugify.forge;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.client.DebugifyClient;
import cc.woverflow.debugify.client.utils.ConfigGuiHelper;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("debugify")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DebugifyMod {
    public DebugifyMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        Debugify.onInitialize();
    }

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
