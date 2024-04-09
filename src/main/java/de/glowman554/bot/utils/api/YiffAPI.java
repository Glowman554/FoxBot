package de.glowman554.bot.utils.api;

import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class YiffAPI {
    private final ArrayList<YiffCategory> categories = new ArrayList<>();

    public YiffAPI() {
        try {
            JsonNode loadedCategories = Json.json().parse(HttpClient.get("https://v2.yiff.rest/categories"));

            for (JsonNode category : loadedCategories.get("data").get("enabled")) {
                categories.add(new YiffCategory(category.get("name").asString(), category.get("db").asString(), category.get("sfw").asBoolean()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<YiffCategory> getCategories() {
        return categories;
    }

    public List<YiffCategory> getCategoriesSFW() {
        return categories.stream().filter(YiffCategory::sfw).toList();
    }

    public List<YiffCategory> getCategoriesNSFW() {
        return categories.stream().filter(Predicate.not(YiffCategory::sfw)).toList();
    }

    public record YiffCategory(String name, String db, boolean sfw) {
        public StreamedFile download() {
            try {
                JsonNode root = Json.json().parse(HttpClient.get(String.format("https://v2.yiff.rest/%s?amount=1&notes=disabled", db.replace(".", "/"))));
                if (!root.get("success").asBoolean()) {
                    int timeout = root.get("info").get("resetAfter").asInt();
                    Logger.log("Timeout: %d", timeout);
                    Thread.sleep(timeout);
                    return download();
                }

                String url = root.get("images").get(0).get("url").asString();

                return HttpClient.download(url);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
