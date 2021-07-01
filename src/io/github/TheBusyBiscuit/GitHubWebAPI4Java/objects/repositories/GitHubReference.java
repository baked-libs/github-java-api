package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;

public class GitHubReference extends GitHubObject {

    GitHubRepository repo;

    public GitHubReference(GitHubWebAPI api, GitHubRepository repo, String id) {
        super(api, repo, "/git/refs/" + id);

        this.repo = repo;
    }

    public GitHubReference(GitHubWebAPI api, GitHubRepository repo, String id, JsonElement response) {
        super(api, repo, "/git/refs/" + id);

        this.repo = repo;
        this.minimal = response;
    }

    public GitHubReference(GitHubObject obj) {
        super(obj);
    }

    public GitHubRepository getRepository() {
        return this.repo;
    }

    @Override
    @GitHubAccessPoint(path = "@object/url", type = String.class, requiresAccessToken = false)
    public String getRawURL() {
        return ".*repos/.*/.*/git/refs/.*";
    }

    @GitHubAccessPoint(path = "@ref", type = String.class, requiresAccessToken = false)
    public String getID() throws IllegalAccessException {
        return getString("ref", false);
    }

    @GitHubAccessPoint(path = "@object/type", type = ReferenceType.class, requiresAccessToken = false)
    public ReferenceType getType() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("object").getAsJsonObject();

        return isInvalid(response, "type") ? null : ReferenceType.valueOf(response.get("type").getAsString().toUpperCase());
    }

    @GitHubAccessPoint(path = "@object/sha", type = String.class, requiresAccessToken = false)
    public String getSHA() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("object").getAsJsonObject();

        return isInvalid(response, "sha") ? null : response.get("sha").getAsString();
    }

    public GitHubCommit getCommit() throws IllegalAccessException {
        if (getType().equals(ReferenceType.COMMIT)) {
            return new GitHubCommit(api, getRepository(), getSHA());
        }

        return null;
    }

    public static enum ReferenceType {

        COMMIT;

    }
}
