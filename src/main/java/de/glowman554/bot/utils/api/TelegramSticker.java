package de.glowman554.bot.utils.api;

import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.HttpClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.ArrayList;

public class TelegramSticker {
    private final String token;

    public TelegramSticker(String token) {
        this.token = token;
    }

    private JsonNode request(String url) {
        Logger.log("requesting: %s", url);
        try {
            String result = HttpClient.get("https://api.telegram.org/bot" + token + "/" + url);

            return Json.json().parse(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> fetchPack(String url) {
        JsonNode files = request("getStickerSet?name=" + url);

        ArrayList<String> urls = new ArrayList<>();
        for (JsonNode node : files.get("result").get("stickers")) {
            JsonNode file = request("getFile?file_id=" + node.get("file_id").asString());
            System.out.println(Json.json().serialize(file));
            urls.add(
                    "https://api.telegram.org/file/bot" + token + "/" + file.get("result").get("file_path").asString());
        }

        return urls;
    }
}
