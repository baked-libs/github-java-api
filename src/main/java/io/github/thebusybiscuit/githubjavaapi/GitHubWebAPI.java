package io.github.thebusybiscuit.githubjavaapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.repositories.GitHubRepository;
import io.github.thebusybiscuit.githubjavaapi.objects.users.GitHubOrganization;
import io.github.thebusybiscuit.githubjavaapi.objects.users.GitHubUser;
import io.github.thebusybiscuit.githubjavaapi.utils.Base64Url;
import io.github.thebusybiscuit.githubjavaapi.utils.CacheMode;

public class GitHubWebAPI {

    private static final String API_URL = "https://api.github.com/";
    public static int ITEMS_PER_PAGE = 100;

    private String accessToken = "";

    protected CacheMode cachingMode;
    protected String hardDriveCachePath = null;
    private final Map<String, JsonElement> memoryCache = new HashMap<>();

    public GitHubWebAPI() {
        this.cachingMode = CacheMode.RAM_CACHE;
    }

    public GitHubWebAPI(CacheMode mode) {
        this.cachingMode = mode;
    }

    public GitHubWebAPI(String accessToken) {
        this.accessToken = accessToken;
        this.cachingMode = CacheMode.RAM_CACHE;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public @Nonnull String getURL() {
        return API_URL;
    }

    public GitHubUser getUser(String username) {
        return new GitHubUser(this, username);
    }

    public GitHubOrganization getOrganization(String username) {
        return new GitHubOrganization(this, username);
    }

    public GitHubRepository getRepository(String username, String repo) {
        return new GitHubRepository(this, username + "/" + repo);
    }

    public JsonElement call(GitHubObject object) {
        try {
            StringBuilder query = new StringBuilder();
            query.append(getURL());
            query.append(object.getURL());

            String token = getAccessToken();

            if (token != null && !token.equals("")) {
                query.append("?access_token=" + token);

                if (object.getParameters() != null) {
                    for (Map.Entry<String, String> parameter : object.getParameters().entrySet()) {
                        query.append("&");
                        query.append(parameter.getKey());
                        query.append("=");
                        query.append(parameter.getValue());
                    }
                }
            } else {
                if (object.getParameters() != null) {
                    boolean first = true;

                    for (Map.Entry<String, String> parameter : object.getParameters().entrySet()) {
                        query.append((first ? "?" : "&"));
                        query.append(parameter.getKey());
                        query.append("=");
                        query.append(parameter.getValue());

                        first = false;
                    }
                }
            }

            URL website = new URL(query.toString());

            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setConnectTimeout(5000);
            connection.addRequestProperty("User-Agent", "GitHub Web API 4 Java (by TheBusyBiscuit)");
            connection.setDoOutput(true);

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder buffer = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                reader.close();
                connection.disconnect();

                return JsonParser.parseString(buffer.toString());
            } else {
                connection.disconnect();

                JsonObject json = new JsonObject();
                json.addProperty("message", connection.getResponseCode() + " - " + connection.getResponseMessage());
                json.addProperty("documentation_url", "https://developer.github.com/v3");
                json.addProperty("code", connection.getResponseCode());

                return json;
            }
        } catch (SocketTimeoutException e) {
            JsonObject json = new JsonObject();
            json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
            json.addProperty("documentation_url", "404");
            json.addProperty("exception", e.getClass().getSimpleName());

            return json;
        } catch (MalformedURLException e) {
            JsonObject json = new JsonObject();
            json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
            json.addProperty("documentation_url", "404");
            json.addProperty("exception", e.getClass().getSimpleName());

            return json;
        } catch (IOException e) {
            JsonObject json = new JsonObject();
            json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
            json.addProperty("documentation_url", "404");
            json.addProperty("exception", e.getClass().getSimpleName());

            return json;
        }
    }

    public void clearCache() {
        this.memoryCache.clear();
    }

    public void cache(String url, JsonElement response) {
        switch (this.cachingMode) {
            case HARD_DRIVE_CACHE:
                if (hardDriveCachePath != null) {
                    try {
                        saveHardDriveCache(Base64Url.encode(url) + ".json", response);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RAM_AND_HARD_DRIVE_CACHE:
                memoryCache.put(url, response);

                if (hardDriveCachePath != null) {
                    try {
                        saveHardDriveCache(Base64Url.encode(url) + ".json", response);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RAM_CACHE:
                memoryCache.put(url, response);
                break;
            default:
                break;
        }
    }

    public String getHardDriveCache() {
        return hardDriveCachePath;
    }

    public @Nullable JsonElement readHardDriveCache(@Nonnull String file) throws IOException {
        File cacheFile = new File(hardDriveCachePath + file);

        if (cacheFile.exists()) {
            String data;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile), StandardCharsets.UTF_8))) {
                data = reader.readLine();
            }

            return JsonParser.parseString(data);
        }

        return null;
    }

    protected void saveHardDriveCache(String file, JsonElement json) throws FileNotFoundException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        PrintWriter writer = new PrintWriter(hardDriveCachePath + file);
        writer.println(gson.toJson(json));
        writer.close();
    }

    public void setupHardDriveCache(String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        this.hardDriveCachePath = path + '/';
    }

    public void setCacheMode(CacheMode mode) {
        this.cachingMode = mode;
    }

    public CacheMode getCacheMode() {
        return this.cachingMode;
    }

    public void disableCaching() {
        this.cachingMode = CacheMode.NO_CACHE;
    }

    public @Nonnull Map<String, JsonElement> getMemoryCache() {
        return memoryCache;
    }
}
