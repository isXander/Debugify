package cc.woverflow.debugify.config;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.utils.ExpectUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DebugifyConfig {
    private final Path configPath = ExpectUtils.getConfigPath().resolve("debugify.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Boolean> jsonBugFixes = new HashMap<>();
    private final Map<String, Boolean> bugFixes = new HashMap<>();

    public void registerBugFix(String id) {
        boolean enabled = jsonBugFixes.getOrDefault(id, true);
        bugFixes.put(id, enabled);
    }

    public void preload() {
        if (!Files.exists(configPath)) {
            return;
        }

        Debugify.logger.info("Preloading Debugify");

        try {
            String jsonString = Files.readString(configPath);
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);

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
        Debugify.logger.info("Saving Debugify");
        try {
            Files.deleteIfExists(configPath);

            JsonObject json = new JsonObject();
            bugFixes.forEach(json::addProperty);

            Files.createFile(configPath);
            Files.writeString(configPath, gson.toJson(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBugFixEnabled(String bug) {
        return bugFixes.getOrDefault(bug, false);
    }

    public Map<String, Boolean> getBugFixes() {
        return bugFixes;
    }
}
