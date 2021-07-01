package io.github.thebusybiscuit.githubjavaapi.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.users.GitHubUser;

public class GitHubEvent extends GitHubObject {

    public GitHubEvent(GitHubWebAPI api, GitHubUser user, String suffix, JsonElement response) {
        super(api, user, suffix);

        this.minimal = response;
    }

    public GitHubEvent(GitHubObject obj) {
        super(obj);
    }

    @GitHubAccessPoint(path = "@id", type = Integer.class, requiresAccessToken = false)
    public int getID() throws IllegalAccessException {
        JsonObject response = (JsonObject) getResponse(false);
        return isInvalid(response, "id") ? null : response.get("id").getAsInt();
    }

    @GitHubAccessPoint(path = "@type", type = String.class, requiresAccessToken = false)
    public String getType() throws IllegalAccessException {
        JsonObject response = (JsonObject) getResponse(false);
        return isInvalid(response, "type") ? null : response.get("type").getAsString();
    }
}