package de.glowman554.bot.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    public static String get(String url, Map<String, String> headers) throws IOException {
        OkHttpClient client = new OkHttpClient();

        var req = new Request.Builder();

        req.url(url);

        req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        req.addHeader("Accept", "application/json");

        for (String key : headers.keySet()) {
            req.addHeader(key, headers.get(key));
        }

        try (Response res = client.newCall(req.build()).execute()) {
            assert res.body() != null;
            return res.body().string();
        }

    }

    public static String get(String url) throws IOException {
        return get(url, new HashMap<>());
    }

    public static String post(String url, RequestBody body, Map<String, String> headers) throws IOException {
        OkHttpClient client = new OkHttpClient();

        var req = new Request.Builder();

        req.url(url);
        req.method("POST", body);

        req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        req.addHeader("Accept", "application/json");

        for (String key : headers.keySet()) {
            req.addHeader(key, headers.get(key));
        }

        try (Response res = client.newCall(req.build()).execute()) {

            assert res.body() != null;
            return res.body().string();
        }
    }

    public static String post(String url, RequestBody body) throws IOException {
        return post(url, body, new HashMap<>());
    }


    public static void download(File file, String url) throws IOException {
        BufferedInputStream in = openStream(url);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        in.transferTo(fileOutputStream);
        in.close();
        fileOutputStream.close();
    }

    public static StreamedFile download(String url) throws IOException {
        URL urlObject = new URL(url);
        BufferedInputStream in = new BufferedInputStream(urlObject.openStream());

        return new StreamedFile(in, urlObject.getFile());
    }

    public static BufferedInputStream openStream(String url) throws IOException {
        URL urlObject = new URL(url);
        return new BufferedInputStream(urlObject.openStream());
    }
}
