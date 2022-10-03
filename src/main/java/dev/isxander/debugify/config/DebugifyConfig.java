package dev.isxander.debugify.config;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFixData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DebugifyConfig {
    private final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("debugify.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, Boolean> jsonBugFixes = new HashMap<>();
    private final Map<BugFixData, Boolean> bugFixes = new TreeMap<>();

    public boolean defaultDisabled = false;
    public boolean optOutUpdater = false;
    public boolean gameplayFixesInMultiplayer = false;

    private boolean preloaded = false;

    public void registerBugFix(BugFixData bugFix) {
        if (bugFixes.containsKey(bugFix))
            return;

        boolean enabled = jsonBugFixes.getOrDefault(bugFix.bugId(), bugFix.enabledByDefault() && !defaultDisabled);
        bugFixes.put(bugFix, enabled);
    }

    public void preload() {
        if (preloaded) return;
        preloaded = true;

        if (!Files.exists(configPath)) {
            if (FabricLoader.getInstance().isModLoaded("yosbr") && Files.exists(configPath.getParent().resolve("yosbr/" + configPath.getFileName().toString()))) {
                throw new YosbrError();
            }
            return;
        }

        Debugify.LOGGER.info("Preloading Debugify");

        try {
            String jsonString = Files.readString(configPath);
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);

            if (json.has("opt_out_updater")) {
                optOutUpdater = json.get("opt_out_updater").getAsBoolean();
            }
            if (json.has("default_disabled")) {
                defaultDisabled = json.get("default_disabled").getAsBoolean();
            }
            if (json.has("gameplay_fixes_in_multiplayer")) {
                gameplayFixesInMultiplayer = json.get("gameplay_fixes_in_multiplayer").getAsBoolean();
            }

            Map<String, Boolean> bugFixes = json.entrySet().stream()
                    .filter((entry) -> entry.getKey().startsWith("MC-") && entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isBoolean())
                    .map((entry) -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getAsBoolean()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            jsonBugFixes.clear();
            jsonBugFixes.putAll(bugFixes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        Debugify.LOGGER.info("Saving Debugify");
        try {
            Files.deleteIfExists(configPath);

            JsonObject json = new JsonObject();
            bugFixes.forEach((fix, enabled) -> {
                if (!defaultDisabled || enabled) json.addProperty(fix.bugId(), enabled);
            });
            json.addProperty("opt_out_updater", optOutUpdater);
            json.addProperty("gameplay_fixes_in_multiplayer", gameplayFixesInMultiplayer);
            json.addProperty("default_disabled", defaultDisabled);

            Files.createFile(configPath);
            Files.writeString(configPath, gson.toJson(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBugFixEnabled(BugFixData bug) {
        return bugFixes.getOrDefault(bug, false);
    }

    public Map<BugFixData, Boolean> getBugFixes() {
        return bugFixes;
    }

    public boolean doesJsonHaveIdenticalKeys() {
        return jsonBugFixes.keySet().containsAll(bugFixes.keySet().stream().map(BugFixData::bugId).collect(Collectors.toSet()))
                && jsonBugFixes.size() == bugFixes.size();
    }

    private static class YosbrError extends Error {
        public YosbrError() {
            super("Debugify config loaded using YOSBR mod! This doesn't work! Please move 'debugify.json' out of the 'yosbr' folder!");
        }
    }
}
