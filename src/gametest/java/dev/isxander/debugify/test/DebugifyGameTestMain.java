package dev.isxander.debugify.test;

import dev.isxander.debugify.Debugify;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.gametest.FabricGameTestHelper;

public class DebugifyGameTestMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Debugify.LOGGER.warn(FabricGameTestHelper.ENABLED +"");
    }
}
