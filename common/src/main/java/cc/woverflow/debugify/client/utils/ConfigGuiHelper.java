package cc.woverflow.debugify.client.utils;

import cc.woverflow.debugify.client.DebugifyClient;
import cc.woverflow.debugify.config.DebugifyConfig;
import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.Optional;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new LiteralText("Debugify"))
                .setSavingRunnable(config::save)
                .setParentScreen(parent);

        config.getBugFixes().forEach((bug, enabled) -> {
            ConfigCategory category = switch (bug.env()) {
                case CLIENT -> builder.getOrCreateCategory(new LiteralText("Client"));
                case SERVER -> builder.getOrCreateCategory(new LiteralText("Server"));
            };

            BooleanToggleBuilder entry = builder.entryBuilder()
                    .startBooleanToggle(new LiteralText(bug.bugId()), enabled)
                    .setSaveConsumer((toggled) -> config.getBugFixes().replace(bug, toggled))
                    .setDefaultValue(true)
                    .setErrorSupplier((b) -> {
                        List<String> conflicts = bug.getActiveConflicts();
                        if (!b || conflicts.isEmpty())
                            return Optional.empty();

                        return Optional.of(new LiteralText(bug.bugId() + " is conflicting with " + (conflicts.size() == 1 ? conflicts.get(0) : "mods " + String.join(", ", conflicts))));
                    })
                    .requireRestart();

            if (DebugifyClient.bugFixDescriptionCache.has(bug.bugId()))
                entry.setTooltip(new LiteralText(DebugifyClient.bugFixDescriptionCache.get(bug.bugId())));

            category.addEntry(entry.build());
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
