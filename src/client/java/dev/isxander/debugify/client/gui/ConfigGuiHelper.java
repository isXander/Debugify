package dev.isxander.debugify.client.gui;

import dev.isxander.debugify.Debugify;
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
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigGuiHelper {
    public static Screen createConfigGui(DebugifyConfig config, Screen parent) {
        var yacl = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("debugify.name"))
                .save(config::save);

        var gameplayWarning = Option.createBuilder(Component.class)
                .binding(Binding.immutable(Component.translatable("debugify.gameplay.warning").withStyle(ChatFormatting.RED)))
                .controller(LabelController::new)
                .build();
        var gameplayInMultiplayer = Option.createBuilder(boolean.class)
                .name(Component.translatable("debugify.gameplay.enable_in_multiplayer"))
                .binding(
                        false,
                        () -> config.gameplayFixesInMultiplayer,
                        value -> config.gameplayFixesInMultiplayer = value
                )
                .controller(TickBoxController::new)
                .build();

        for (BugFix.Env env : BugFix.Env.values()) {
            var categoryBuilder = ConfigCategory.createBuilder()
                    .name(Component.translatable(env.getDisplayName()))
                    .tooltip(Component.translatable(env.getDescriptionKey()));

            for (FixCategory fixCategory : FixCategory.values()) {
                var groupBuilder = OptionGroup.createBuilder()
                        .name(Component.translatable(fixCategory.getDisplayName()));

                if (fixCategory == FixCategory.GAMEPLAY) {
                    groupBuilder
                            .option(gameplayWarning)
                            .option(gameplayInMultiplayer);
                }

                config.getBugFixes().forEach((bug, enabled) -> {
                    if (bug.env() == env && bug.category() == fixCategory) {
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

                        String fixExplanationTooltipKey = "debugify.fix_explanation." + bug.bugId().toLowerCase();
                        if (Language.getInstance().has(fixExplanationTooltipKey))
                            optionBuilder.tooltip(Component.translatable(fixExplanationTooltipKey).withStyle(ChatFormatting.GRAY));

                        String fixEffectTooltipKey = "debugify.fix_effect." + bug.bugId().toLowerCase();
                        if (Language.getInstance().has(fixEffectTooltipKey))
                            optionBuilder.tooltip(Component.translatable(fixEffectTooltipKey).withStyle(ChatFormatting.GOLD));

                        for (String conflictMod : conflicts) {
                            optionBuilder.tooltip(Component.translatable("debugify.error.conflict", bug.bugId(), conflictMod).withStyle(ChatFormatting.RED));
                        }

                        if (!satisfiesOS)
                            optionBuilder.tooltip(Component.translatable("debugify.error.os", bug.bugId(), Component.translatable(bug.requiredOs().getDisplayName())).withStyle(ChatFormatting.RED));

                        groupBuilder.option(optionBuilder.build());
                    }
                });

                categoryBuilder.group(groupBuilder.build());
            }

            yacl.category(categoryBuilder.build());
        }

        yacl.category(ConfigCategory.createBuilder()
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
                .build());

        return yacl.build().generateScreen(parent);
    }
}
