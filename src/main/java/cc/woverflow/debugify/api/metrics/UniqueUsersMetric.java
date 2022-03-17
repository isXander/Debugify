package cc.woverflow.debugify.api.metrics;

import cc.woverflow.debugify.Debugify;
import com.mojang.authlib.minecraft.UserApiService;
import cc.woverflow.debugify.mixins.metrics.AccessorMinecraftClient;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UniqueUsersMetric {
    public static void putApi() {
        if (((AccessorMinecraftClient) MinecraftClient.getInstance()).getUserApiService() == UserApiService.OFFLINE) {
            return;
        }

        try {
            URI url = URI.create("https://api.isxander.dev/metric/put/debugify?type=unique_users&uuid=" + MinecraftClient.getInstance().getSession().getProfile().getId());

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(url)
                    .header("User-Agent", "Debugify")
                    .header("accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("[Debugify] Failed to put unique users metric: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
