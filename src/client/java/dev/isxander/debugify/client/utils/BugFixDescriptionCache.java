package dev.isxander.debugify.client.utils;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFixData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BugFixDescriptionCache {
    private static final Path file = FabricLoader.getInstance().getConfigDir().resolve("debugify-descriptions.json");
    private static final Gson gson = new Gson();
    private final Map<String, String> descriptionHolder = new HashMap<>();
    private final String url = "https://bugs.mojang.com/rest/api/2/issue/%s";

    private void cacheDescriptions() {
        Debugify.logger.info("Caching bug descriptions");

        HttpClient client = HttpClient.newHttpClient();

        for (BugFixData bugData : Debugify.config.getBugFixes().keySet()) {
            String id = bugData.bugId();
            try {
                HttpRequest request = HttpRequest.newBuilder(new URI(String.format(url, id)))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    Debugify.logger.error("Description Cache: {} - {}", response.statusCode(), response.body());
                    continue;
                }

                JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                JsonObject fields = json.getAsJsonObject("fields");
                String summary = fields.get("summary").getAsString();

                descriptionHolder.put(id, !FabricLoader.getInstance().isModLoaded("tooltipfix") ? wrapTextLenient(summary, 50) : summary);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void trySave() {
        Debugify.logger.info("Saving Description Cache...");
        if (descriptionHolder.isEmpty()) {
            Debugify.logger.info("Cache empty. Getting descriptions and saving in a new thread.");
            CompletableFuture.runAsync(() -> {
                cacheDescriptions();
                save();
            });
            return;
        }

        save();
    }

    private void save() {
        try {
            Files.deleteIfExists(file);

            JsonObject json = new JsonObject();
            descriptionHolder.forEach(json::addProperty);

            Files.createFile(file);
            Files.writeString(file, gson.toJson(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load() {
        Debugify.logger.info("Loading Description Cache");

        if (!Files.exists(file)) {
            return false;
        }

        try {
            String jsonString = Files.readString(file);
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);

            Map<String, String> loadedDescriptions = json.entrySet().stream()
                    .map((entry) -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getAsString()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            descriptionHolder.putAll(loadedDescriptions);
        } catch (Exception e) {
            Debugify.logger.error("Couldn't load description cache! Attempting to save.");
            e.printStackTrace();
            trySave();
            return false;
        }

        return true;
    }

    public String get(String id) {
        return descriptionHolder.get(id);
    }

    public boolean has(String id) {
        return descriptionHolder.containsKey(id);
    }

    private String wrapTextLenient(String text, int charLength) {
        StringBuilder sb = new StringBuilder();
        int lineLength = 0;
        boolean needsLineBreak = false;
        for (char c : text.toCharArray()) {
            lineLength += 1;
            if (c == '\n') lineLength = 0;
            if (lineLength > charLength) {
                needsLineBreak = true;
            }
            if (needsLineBreak && c == ' ') {
                lineLength = 0;
                sb.append('\n');
                needsLineBreak = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
