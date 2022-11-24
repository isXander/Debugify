package dev.isxander.debugify.client.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.gui.ConfigGuiHelper;
import dev.isxander.debugify.client.gui.NoYACLScreen;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> {
            if (!FabricLoader.getInstance().isModLoaded("yet-another-config-lib"))
                return new NoYACLScreen(parent);
            return ConfigGuiHelper.createConfigGui(Debugify.CONFIG, parent);
        };
    }
}
