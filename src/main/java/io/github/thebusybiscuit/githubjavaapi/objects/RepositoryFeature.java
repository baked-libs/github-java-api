package io.github.thebusybiscuit.githubjavaapi.objects;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;

public class RepositoryFeature extends UniqueGitHubObject {

    public RepositoryFeature(GitHubWebAPI api, GitHubObject parent, String suffix) {
        super(api, parent, suffix);
    }

    public RepositoryFeature(GitHubObject obj) {
        super(obj);
    }

    @GitHubAccessPoint(path = "@number", type = Integer.class, requiresAccessToken = false)
    public int getNumber() throws IllegalAccessException {
        return getInteger("number", false);
    }

    @GitHubAccessPoint(path = "@title", type = String.class, requiresAccessToken = false)
    public String getTitle() throws IllegalAccessException {
        return getString("title", false);
    }

    @GitHubAccessPoint(path = "@closed_at", type = Date.class, requiresAccessToken = false)
    public Date getClosedDate() throws IllegalAccessException {
        return getDate("closed_at", true);
    }

    @GitHubAccessPoint(path = "@state", type = State.class, requiresAccessToken = false)
    public State getState() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "state") ? null : State.valueOf(response.get("state").getAsString().toUpperCase());
    }

    public static enum State {

        OPEN,
        CLOSED;

    }

}
