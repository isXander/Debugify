package cc.woverflow.debugify.fabric.integrations;

import cc.woverflow.debugify.Debugify;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> Debugify.config.createConfigGui(parent);
    }
}
