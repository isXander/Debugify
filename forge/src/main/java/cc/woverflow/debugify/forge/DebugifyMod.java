package cc.woverflow.debugify.forge;

import cc.woverflow.debugify.Debugify;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("debugify")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DebugifyMod {
    public DebugifyMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);

        if (FMLEnvironment.dist == Dist.CLIENT)
            FMLJavaModLoadingContext.get().getModEventBus().register(new ClientEvents());
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        Debugify.onInitialize();
    }
}
