package de.glowman554.bot.utils.api.openai;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import okhttp3.*;

import java.util.ArrayList;

public class ChatCompletion {
    private final String apiKey;
    private String model = "gpt-4o-mini";

    private final OkHttpClient client = new OkHttpClient();

    public ChatCompletion(String apiKey) {
        this.apiKey = apiKey;
    }

    public Message[] requestCompletion(Message[] messages) {
        JsonNode root = JsonNode.object()
                .set("model", model)
                .set("messages", JsonNode.array());

        for (Message message : messages) {
            root.get("messages").add(message.toJSON());
        }

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(root.toString(), MediaType.get("application/json")))
                .build();

        try (Response res = client.newCall(request).execute()) {
            assert res.body() != null;

            JsonNode body = Json.json().parse(res.body().string());

            ArrayList<Message> choices = new ArrayList<>();
            for (JsonNode choiceNode : body.get("choices")) {
                Choice choice = new Choice();
                choice.fromJSON(choiceNode);
                choices.add(choice.message);
            }

            return choices.toArray(Message[]::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setModel(String model) {
        this.model = model;
    }

    public static class Message extends AutoSavable {
        @Saved
        private String role;
        @Saved
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public Message() {
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }


    private static class Choice extends AutoSavable {
        @Saved
        public int index;
        @Saved(remap = Savable.class)
        public Message message = new Message();
        @Saved
        public String finish_reason;
    }

}
