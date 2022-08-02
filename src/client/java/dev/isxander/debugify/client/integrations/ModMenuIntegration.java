package dev.isxander.debugify.client.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.utils.ConfigGuiHelper;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> ConfigGuiHelper.createConfigGui(Debugify.config, parent);
    }
}
