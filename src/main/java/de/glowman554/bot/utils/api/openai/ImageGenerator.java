package de.glowman554.bot.utils.api.openai;

import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class ImageGenerator {
    private final String apiKey;
    private String model = "dall-e-3";

    private final OkHttpClient client;


    public ImageGenerator(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    public String requestImage(String prompt) {
        JsonNode root = JsonNode.object()
                .set("model", model)
                .set("prompt", prompt)
                .set("n", 1)
                .set("size", "1024x1024");

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(root.toString(), MediaType.get("application/json")))
                .build();

        try (Response res = client.newCall(request).execute()) {
            assert res.body() != null;

            JsonNode body = Json.json().parse(res.body().string());

            return body.get("data").get(0).get("url").asString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setModel(String model) {
        this.model = model;
    }

}
