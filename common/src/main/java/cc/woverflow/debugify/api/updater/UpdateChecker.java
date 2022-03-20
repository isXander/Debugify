package cc.woverflow.debugify.api.updater;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.utils.Loader;
import com.github.zafarkhaja.semver.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.SharedConstants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateChecker {
    private static final Gson gson = new Gson();

    public static Version getLatestVersion() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("https://api.isxander.dev/updater/latest/debugify?loader=%s&minecraft=%s", Loader.getLoader().id, SharedConstants.getGameVersion().getName())))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response.statusCode() != 200) {
            Debugify.logger.error("Couldn't get latest version!");
            return null;
        }

        JsonObject json = gson.fromJson(response.body(), JsonObject.class);
        return Version.valueOf(json.get("version").getAsString());
    }
}
