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

    public void cacheDescriptions() {
        Debugify.LOGGER.info("Connecting to 'bugs.mojang.com' to cache bug descriptions!");

        CompletableFuture.runAsync(() -> {
            HttpClient client = HttpClient.newHttpClient();

            for (BugFixData bugData : Debugify.CONFIG.getBugFixes().keySet()) {
                String id = bugData.bugId();
                try {
                    HttpRequest request = HttpRequest.newBuilder(new URI(String.format(url, id)))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() != 200) {
                        Debugify.LOGGER.error("Description Cache: {} - {}", response.statusCode(), response.body());
                        continue;
                    }

                    JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                    JsonObject fields = json.getAsJsonObject("fields");
                    String summary = fields.get("summary").getAsString();

                    descriptionHolder.put(id, summary);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).thenAccept((returnVal) -> save());
    }

    public void save() {
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
        Debugify.LOGGER.info("Loading Description Cache");

        if (!Files.exists(file)) {
            return false;
        }

        try {
            String jsonString = Files.readString(file);
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);

            Map<String, String> loadedDescriptions = json.entrySet().stream()
                    .map((entry) -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getAsString()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (loadedDescriptions.values().stream().anyMatch(desc -> desc.contains("\n"))) {
                Debugify.LOGGER.warn("Outdated description cache format, re-caching!");
                cacheDescriptions();
                return true;
            }

            descriptionHolder.putAll(loadedDescriptions);
        } catch (Exception e) {
            Debugify.LOGGER.error("Couldn't load description cache!");
            e.printStackTrace();
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
}
