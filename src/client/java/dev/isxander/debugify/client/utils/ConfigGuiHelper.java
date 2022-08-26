package dev.isxander.debugify.client.utils;

import dev.isxander.debugify.client.DebugifyClient;
import dev.isxander.debugify.config.DebugifyConfig;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Text.translatable("debugify.name"))
                .setSavingRunnable(config::save)
                .setParentScreen(parent);

        Map<FixCategory, ConfigCategory> fixCategories = new HashMap<>();
        Map<ConfigCategory, Map<BugFix.Env, SubCategoryBuilder>> fixSubCategories = new HashMap<>();
        for (FixCategory fixCategory : FixCategory.values()) {
            var configCategory = builder.getOrCreateCategory(Text.translatable(fixCategory.getDisplayName()));
            if (fixCategory == FixCategory.GAMEPLAY) {
                configCategory.addEntry(builder.entryBuilder()
                        .startTextDescription(Text.translatable("debugify.gameplay.warning").formatted(Formatting.RED))
                        .build()
                );
                configCategory.addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.translatable("debugify.gameplay.enable_in_multiplayer"), config.gameplayFixesInMultiplayer)
                        .setSaveConsumer((enabled) -> config.gameplayFixesInMultiplayer = enabled)
                        .build()
                );
            }

            fixCategories.put(fixCategory, configCategory);

            Map<BugFix.Env, SubCategoryBuilder> subCategories = new HashMap<>();
            for (BugFix.Env env : BugFix.Env.values()) {
                var subCategoryBuilder = builder.entryBuilder().startSubCategory(Text.translatable(env.getDisplayName()));
                subCategories.put(env, subCategoryBuilder);
            }
            fixSubCategories.put(configCategory, subCategories);
        }

        config.getBugFixes().forEach((bug, enabled) -> {
            SubCategoryBuilder subcategory = fixSubCategories.get(fixCategories.get(bug.category())).get(bug.env());

            BooleanToggleBuilder entry = builder.entryBuilder()
                    .startBooleanToggle(Text.literal(bug.bugId()), enabled)
                    .setSaveConsumer((toggled) -> config.getBugFixes().replace(bug, toggled))
                    .setDefaultValue(bug.enabledByDefault())
                    .setErrorSupplier((b) -> {
                        List<String> conflicts = bug.getActiveConflicts();
                        if (!b || conflicts.isEmpty())
                            return Optional.empty();

                        if (conflicts.size() == 1)
                            return Optional.of(Text.translatable("debugify.error.conflict.single", bug.bugId(), conflicts.get(0)));
                        else
                            return Optional.of(Text.translatable("debugify.error.conflict.multiple", bug.bugId(), String.join(", ", conflicts)));
                    })
                    .requireRestart();

            if (DebugifyClient.bugFixDescriptionCache.has(bug.bugId()))
                entry.setTooltip(Text.literal(DebugifyClient.bugFixDescriptionCache.get(bug.bugId())));

            subcategory.add(entry.build());
        });
        fixSubCategories.forEach((category, subCategories) ->
                subCategories.forEach((env, subCategoryBuilder) -> category.addEntry(subCategoryBuilder.build())));

        ConfigCategory miscCategory = builder.getOrCreateCategory(Text.translatable("debugify.misc"));
        AbstractConfigListEntry<?> defaultDisabledEntry = builder.entryBuilder()
                .startBooleanToggle(Text.translatable("debugify.misc.default_disabled"), config.defaultDisabled)
                .setTooltip(Text.translatable("debugify.misc.default_disabled.description"))
                .setSaveConsumer((toggled) -> config.defaultDisabled = toggled)
                .setDefaultValue(false)
                .build();
        miscCategory.addEntry(defaultDisabledEntry);

        builder.setAfterInitConsumer((screen) -> {
            var text = Text.translatable("debugify.donate");
            var width = MinecraftClient.getInstance().textRenderer.getWidth(text) + 8;
            Screens.getButtons(screen).add(new ButtonWidget(screen.width - width - 4, 4, width, 20, text, (button) -> {
                Util.getOperatingSystem().open("https://ko-fi.com/isxander");
            }));
        });

        return builder.build();
    }
}
