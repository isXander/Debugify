package cc.woverflow.debugify.client.utils;

import cc.woverflow.debugify.client.DebugifyClient;
import cc.woverflow.debugify.config.DebugifyConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new LiteralText("Debugify"))
                .setSavingRunnable(config::save)
                .setParentScreen(parent);

        ConfigCategory fixesCategory = builder.getOrCreateCategory(new LiteralText("Fixes"));
        config.getBugFixes().forEach((bug, enabled) -> {
            BooleanToggleBuilder entry = builder.entryBuilder()
                    .startBooleanToggle(new LiteralText(bug), enabled)
                    .setSaveConsumer((toggled) -> config.getBugFixes().replace(bug, toggled))
                    .setDefaultValue(true)
                    .requireRestart();

            if (DebugifyClient.bugFixDescriptionCache.has(bug))
                entry.setTooltip(new LiteralText(DebugifyClient.bugFixDescriptionCache.get(bug)));

            fixesCategory.addEntry(entry.build());
        });

        ConfigCategory miscCategory = builder.getOrCreateCategory(new LiteralText("Misc"));
        AbstractConfigListEntry<?> optOutUpdaterEntry = builder.entryBuilder()
                .startBooleanToggle(new LiteralText("Opt Out Updater"), config.optOutUpdater)
                .setTooltip(new LiteralText("Stop Debugify checking for updates on launch."))
                .setSaveConsumer((toggled) -> config.optOutUpdater = toggled)
                .setDefaultValue(false)
                .build();
        miscCategory.addEntry(optOutUpdaterEntry);

        AbstractConfigListEntry<?> defaultDisabledEntry = builder.entryBuilder()
                .startBooleanToggle(new LiteralText("Default to Disabled"), config.defaultDisabled)
                .setTooltip(new LiteralText("Default new bug fixes to be disabled rather than enabled."))
                .setSaveConsumer((toggled) -> config.defaultDisabled = toggled)
                .setDefaultValue(false)
                .build();
        miscCategory.addEntry(defaultDisabledEntry);

        return builder.build();
    }
}
