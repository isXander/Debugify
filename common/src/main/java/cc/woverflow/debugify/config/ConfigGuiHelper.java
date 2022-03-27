package cc.woverflow.debugify.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new LiteralText("Debugify"))
                .setSavingRunnable(config::save)
                .setParentScreen(parent);

        ConfigCategory category = builder.getOrCreateCategory(new LiteralText("Fixes"));
        config.getBugFixes().forEach((bug, enabled) -> {
            AbstractConfigListEntry<?> entry = builder.entryBuilder()
                    .startBooleanToggle(new LiteralText(bug), enabled)
                    .setSaveConsumer((toggled) -> config.getBugFixes().replace(bug, toggled))
                    .setDefaultValue(true)
                    .requireRestart()
                    .build();

            category.addEntry(entry);
        });

        return builder.build();
    }
}
