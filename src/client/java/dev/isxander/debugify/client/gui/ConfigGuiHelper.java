package dev.isxander.debugify.client.gui;

import dev.isxander.debugify.client.DebugifyClient;
import dev.isxander.debugify.config.DebugifyConfig;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.yacl.api.*;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.LabelController;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        Map<FixCategory, ConfigCategory.Builder> fixCategories = new LinkedHashMap<>();
        Map<ConfigCategory.Builder, Map<BugFix.Env, OptionGroup.Builder>> fixGroups = new LinkedHashMap<>();
        for (FixCategory fixCategory : FixCategory.values()) {
            var categoryBuilder = ConfigCategory.createBuilder()
                    .name(Component.translatable(fixCategory.getDisplayName()));

            if (fixCategory == FixCategory.GAMEPLAY) {
                categoryBuilder
                        .option(Option.createBuilder(Component.class)
                                .binding(Binding.immutable(Component.translatable("debugify.gameplay.warning").withStyle(ChatFormatting.RED)))
                                .controller(LabelController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Component.translatable("debugify.gameplay.enable_in_multiplayer"))
                                .binding(
                                        false,
                                        () -> config.gameplayFixesInMultiplayer,
                                        value -> config.gameplayFixesInMultiplayer = value
                                )
                                .controller(TickBoxController::new)
                                .build());
            }

            fixCategories.put(fixCategory, categoryBuilder);

            Map<BugFix.Env, OptionGroup.Builder> envGroups = new LinkedHashMap<>();
            for (BugFix.Env env : BugFix.Env.values()) {
                var groupBuilder = OptionGroup.createBuilder()
                        .name(Component.translatable(env.getDisplayName()))
                        .collapsed(true);
                envGroups.put(env, groupBuilder);
            }
            fixGroups.put(categoryBuilder, envGroups);
        }

        config.getBugFixes().forEach((bug, enabled) -> {
            var conflicts = bug.getActiveConflicts().stream().map(id -> FabricLoader.getInstance().getModContainer(id).orElseThrow().getMetadata().getName()).toList();
            var satisfiesOS = bug.satisfiesOSRequirement();
            var unavailable = !conflicts.isEmpty() || !satisfiesOS;

            var optionBuilder = Option.createBuilder(boolean.class)
                    .name(Component.literal(bug.bugId()))
                    .binding(
                            bug.enabledByDefault(),
                            () -> config.getBugFixes().get(bug),
                            value -> config.getBugFixes().replace(bug, value)
                    )
                    .controller(BugFixController::new)
                    .available(!unavailable)
                    .flag(OptionFlag.GAME_RESTART);

            if (DebugifyClient.bugFixDescriptionCache.has(bug.bugId()))
                optionBuilder.tooltip(Component.literal(DebugifyClient.bugFixDescriptionCache.get(bug.bugId())));

            if (!conflicts.isEmpty())
                optionBuilder.tooltip(Component.translatable("debugify.error.conflict", bug.bugId(), String.join(", ", conflicts)).withStyle(ChatFormatting.RED));

            if (!satisfiesOS)
                optionBuilder.tooltip(Component.translatable("debugify.error.os", bug.bugId(), Component.translatable(bug.requiredOs().getDisplayName())).withStyle(ChatFormatting.RED));

            fixGroups.get(fixCategories.get(bug.category())).get(bug.env())
                    .option(optionBuilder.build());
        });

        fixGroups.forEach((category, groups) ->
                groups.forEach((env, groupBuilder) -> category.group(groupBuilder.build())));

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("debugify.name"))
                .save(config::save)
                .categories(fixCategories.values().stream().map(ConfigCategory.Builder::build).toList())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("debugify.misc"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Component.translatable("debugify.misc.default_disabled"))
                                .tooltip(Component.translatable("debugify.misc.default_disabled.description"))
                                .binding(
                                        false,
                                        () -> config.defaultDisabled,
                                        value -> config.defaultDisabled = value
                                )
                                .controller(BooleanController::new)
                                .build())
                        .build())
                .build().generateScreen(parent);
    }
}
