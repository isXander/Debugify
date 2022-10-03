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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        Map<FixCategory, ConfigCategory.Builder> fixCategories = new LinkedHashMap<>();
        Map<ConfigCategory.Builder, Map<BugFix.Env, OptionGroup.Builder>> fixGroups = new LinkedHashMap<>();
        for (FixCategory fixCategory : FixCategory.values()) {
            var categoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable(fixCategory.getDisplayName()));

            if (fixCategory == FixCategory.GAMEPLAY) {
                categoryBuilder
                        .option(Option.createBuilder(Text.class)
                                .binding(Binding.immutable(Text.translatable("debugify.gameplay.warning").formatted(Formatting.RED)))
                                .controller(LabelController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.translatable("debugify.gameplay.enable_in_multiplayer"))
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
                        .name(Text.translatable(env.getDisplayName()))
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
                    .name(Text.of(bug.bugId()))
                    .binding(
                            bug.enabledByDefault(),
                            () -> config.getBugFixes().get(bug),
                            value -> config.getBugFixes().replace(bug, value)
                    )
                    .controller(opt -> new BooleanController(opt, state -> {
                        if (unavailable)
                            return Text.translatable("debugify.fix.unavailable");
                        return state
                                ? Text.translatable("debugify.fix.enabled").formatted(Formatting.GREEN)
                                : Text.translatable("debugify.fix.disabled").formatted(Formatting.RED);
                    }, false))
                    .available(!unavailable)
                    .flag(OptionFlag.GAME_RESTART);

            if (DebugifyClient.bugFixDescriptionCache.has(bug.bugId()))
                optionBuilder.tooltip(Text.literal(DebugifyClient.bugFixDescriptionCache.get(bug.bugId())));

            if (!conflicts.isEmpty())
                optionBuilder.tooltip(Text.translatable("debugify.error.conflict", bug.bugId(), String.join(", ", conflicts)).formatted(Formatting.RED));

            if (!satisfiesOS)
                optionBuilder.tooltip(Text.translatable("debugify.error.os", bug.bugId(), Text.translatable(bug.requiredOs().getDisplayName())).formatted(Formatting.RED));

            fixGroups.get(fixCategories.get(bug.category())).get(bug.env())
                    .option(optionBuilder.build());
        });

        fixGroups.forEach((category, groups) ->
                groups.forEach((env, groupBuilder) -> category.group(groupBuilder.build())));

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("debugify.name"))
                .save(config::save)
                .categories(fixCategories.values().stream().map(ConfigCategory.Builder::build).toList())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("debugify.misc"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.translatable("debugify.misc.default_disabled"))
                                .tooltip(Text.translatable("debugify.misc.default_disabled.description"))
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
