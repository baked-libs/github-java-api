package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;

public class GitHubLicense extends GitHubObject {

    private String param;

    public GitHubLicense(GitHubWebAPI api, String key, JsonObject obj) throws UnsupportedEncodingException {
        super(api, null, "/licenses/" + URLEncoder.encode(key, "utf-8"));

        this.param = URLEncoder.encode(key, "utf-8");
        this.minimal = obj;
    }

    public GitHubLicense(GitHubObject obj) {
        super(obj);
    }

    @Override
    public String getRawURL() {
        return ".*licenses/.*";
    }

    @GitHubAccessPoint(path = "@key", type = String.class, requiresAccessToken = false)
    public String getKey() throws IllegalAccessException {
        return getString("key", false);
    }

    @GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
    public String getName() throws IllegalAccessException {
        return getString("name", false);
    }

    @GitHubAccessPoint(path = "@spdx_id", type = String.class, requiresAccessToken = false)
    public String getSpdxID() throws IllegalAccessException {
        return getString("spdx_id", false);
    }

    @GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
    public String getDescription() throws IllegalAccessException {
        return getString("description", true);
    }

    @GitHubAccessPoint(path = "@implementation", type = String.class, requiresAccessToken = false)
    public String getImplementation() throws IllegalAccessException {
        return getString("implementation", true);
    }

    @GitHubAccessPoint(path = "@body", type = String.class, requiresAccessToken = false)
    public String getFullBody() throws IllegalAccessException {
        return getString("body", true);
    }

    @GitHubAccessPoint(path = "@featured", type = Boolean.class, requiresAccessToken = false)
    public Boolean isFeatured() throws IllegalAccessException {
        return getBoolean("featured", true);
    }

    public String getURLEncodedParameter() {
        return this.param;
    }

    private List<String> getList(String path) throws IllegalAccessException {
        JsonElement response = getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<String> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            list.add(array.get(i).getAsString());
        }

        return list;
    }

    @GitHubAccessPoint(path = "@permissions", type = String.class, requiresAccessToken = false)
    public List<String> getPermissions() throws IllegalAccessException {
        return getList("permissions");
    }

    @GitHubAccessPoint(path = "@conditions", type = String.class, requiresAccessToken = false)
    public List<String> getConditions() throws IllegalAccessException {
        return getList("conditions");
    }

    @GitHubAccessPoint(path = "@limitations", type = String.class, requiresAccessToken = false)
    public List<String> getLimitations() throws IllegalAccessException {
        return getList("limitations");
    }

}
