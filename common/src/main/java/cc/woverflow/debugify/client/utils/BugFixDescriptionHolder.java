package cc.woverflow.debugify.client.utils;

import cc.woverflow.debugify.Debugify;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BugFixDescriptionHolder {
    private static final Map<String, String> descriptionHolder = new HashMap<>();
    private static final String url = "https://bugs.mojang.com/rest/api/2/issue/%s";
    private static final Gson gson = new Gson();

    public static void cacheDescriptions() {
        CompletableFuture.runAsync(() -> {
            Debugify.logger.info("Caching bug descriptions");

            HttpClient client = HttpClient.newHttpClient();

            for (String id : Debugify.config.getBugFixes().keySet()) {
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

                    descriptionHolder.put(id, wrapTextLenient(summary, 50));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String get(String id) {
        return descriptionHolder.get(id);
    }

    private static String wrapTextLenient(String text, int charLength) {
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
