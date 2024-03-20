package de.glowman554.bot.utils.api.spotify;

import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.event.impl.JavalinEvent;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.AutoFileSavable;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.Saved;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

public class SpotifyApi implements AutoCloseable {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;

    private final OkHttpClient client = new OkHttpClient();


    private final Timer timer = new Timer("Spotify renew timer");

    private SpotifyToken spotifyToken = new SpotifyToken();
    private boolean authentication = false;

    public SpotifyApi(String clientId, String clientSecret, String redirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;

        spotifyToken.load();
        if (spotifyToken.isEmpty()) {
            Logger.log("Spotify authentication necessary.");
            authentication = true;
        } else {
            setup();
        }

        EventManager.register(this);
    }

    @EventTarget
    public void onJavalin(JavalinEvent event) {
        event.getJavalin().get("/login", (context) -> {
            context.redirect("https://accounts.spotify.com/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl);
        });

        event.getJavalin().get("/callback", (context) -> {
            String code = context.queryParams("code").stream().findFirst().orElseThrow();

            spotifyToken = createToken(code);
            spotifyToken.save();
            setup();

            context.result("Big success!");
        });
    }

    private SpotifyToken createToken(String code) {
        RequestBody formBody = new FormBody.Builder().add("grant_type", "authorization_code").add("code", code).add("redirect_uri", redirectUrl).build();
        Request request = new Request.Builder().url("https://accounts.spotify.com/api/token").addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes())).post(formBody).build();

        try (Response res = client.newCall(request).execute()) {
            assert res.body() != null;

            JsonNode body = Json.json().parse(res.body().string());
            SpotifyToken tokens = new SpotifyToken();
            tokens.fromJSON(body);
            return tokens;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SpotifyToken refreshToken(SpotifyToken current) {
        RequestBody formBody = new FormBody.Builder().add("grant_type", "refresh_token").add("refresh_token", spotifyToken.refresh_token).build();
        Request request = new Request.Builder().url("https://accounts.spotify.com/api/token").addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes())).post(formBody).build();

        try (Response res = client.newCall(request).execute()) {
            assert res.body() != null;

            JsonNode body = Json.json().parse(res.body().string());
            SpotifyToken tokens = new SpotifyToken();
            tokens.fromJSON(body);
            tokens.refresh_token = current.refresh_token;
            return tokens;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request prepareSpotifyApiGetRequest(String url) {
        return new Request.Builder().url("https://api.spotify.com" + url).addHeader("Authorization", "Bearer " + spotifyToken.access_token).get().build();
    }

    public List<Song> searchSpotifySongs(String query, int limit) {
        Request request = prepareSpotifyApiGetRequest("/v1/search?q=" + URLEncoder.encode(query, Charset.defaultCharset()) + "&type=track&limit=" + limit);

        try (Response res = client.newCall(request).execute()) {
            assert res.body() != null;

            JsonNode body = Json.json().parse(res.body().string());

            List<Song> songs = new ArrayList<>();
            for (JsonNode entry : body.get("tracks").get("items")) {
                songs.add(new Song(entry.get("external_urls").get("spotify").asString(), entry.get("name").asString(), entry.get("preview_url").asString()));
            }
            return songs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setup() {
        Logger.log("Setting up spotify api");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spotifyToken = refreshToken(spotifyToken);
                spotifyToken.save();
            }
        }, 0, 1000 * 60);
    }

    @Override
    public void close() throws Exception {
        timer.cancel();
    }

    private static class SpotifyToken extends AutoFileSavable {
        private final File configFile = new File(ConfigManager.BASE_FOLDER, "spotify.json");
        @Saved
        private String access_token = "";
        @Saved
        private String token_type = "";
        @Saved
        private int expires_in = 0;
        @Saved
        private String refresh_token = "";

        public void load() {
            load(configFile);
        }

        public void save() {
            save(configFile);
        }

        public boolean isEmpty() {
            return access_token.isEmpty() || token_type.isEmpty() || expires_in == 0 || refresh_token.isEmpty();
        }
    }
}
