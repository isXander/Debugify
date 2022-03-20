package cc.woverflow.debugify.config;

import cc.woverflow.debugify.Debugify;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;

import java.io.*;

public class DebugifyConfig {
    public static File debugifyConfig = new File(MinecraftClient.getInstance().runDirectory, "config/debugify.json");
    private static Gson gson = new Gson();

    public boolean doMetrics = true;

    public void save() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("metrics", doMetrics);

        debugifyConfig.delete();
        new FileOutputStream(debugifyConfig).write(gson.toJson(json).getBytes());
    }

    public void load() {
        try {
            if (!debugifyConfig.exists()) {
                save();
                return;
            }

            JsonObject json = gson.fromJson(new FileReader(debugifyConfig), JsonObject.class);
            doMetrics = json.get("metrics").getAsBoolean();
        } catch (Exception e) {
            Debugify.logger.info("Failed to read debugify config, setting doMetrics to false just in case.");
            doMetrics = false;
            e.printStackTrace();
        }
    }
}
