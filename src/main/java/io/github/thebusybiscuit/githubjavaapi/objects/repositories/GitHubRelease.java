package io.github.thebusybiscuit.githubjavaapi.objects.repositories;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.users.GitHubUser;

/**
 * Represents a Release from a GitHubRepository.
 * 
 * @since 1.3.4
 */
public class GitHubRelease extends GitHubObject {

    public GitHubRelease(GitHubWebAPI api, GitHubRepository repo, int id) {
        super(api, repo, "/releases/" + id);
    }

    public GitHubRelease(GitHubWebAPI api, GitHubRepository repo, int id, JsonElement response) {
        super(api, repo, "/releases/" + id);

        this.minimal = response;
    }

    public GitHubRelease(GitHubObject obj) {
        super(obj);
    }

    /**
     * Returns the ID of this release.
     * 
     * @return the ID of this release.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@id", type = Integer.class, requiresAccessToken = false)
    public int getID() throws IllegalAccessException {
        return getInteger("id", false);
    }

    /**
     * Returns whether this release is a draft (i.e. not published yet) or not.
     * 
     * @return {@code true} if this release is a draft, {@code false} otherwise.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@draft", type = Boolean.class, requiresAccessToken = false)
    public boolean isDraft() throws IllegalAccessException {
        return getBoolean("draft", false);
    }

    /**
     * Returns whether this release is indicated as a pre-release or not.
     * 
     * @return {@code true} if this release is a pre-release, {@code false} otherwise.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@prerelease", type = Boolean.class, requiresAccessToken = false)
    public boolean isPreRelease() throws IllegalAccessException {
        return getBoolean("prerelease", false);
    }

    /**
     * Returns the name of the git tag corresponding to this release.
     * 
     * @return the name of the git tag corresponding to this release.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@tag_name", type = String.class, requiresAccessToken = false)
    public String getTagName() throws IllegalAccessException {
        return getString("tag_name", false);
    }

    @GitHubAccessPoint(path = "@target_commitish", type = String.class, requiresAccessToken = false)
    public String getTargetCommitish() throws IllegalAccessException {
        return getString("target_commitish", false);
    }

    /**
     * Returns the name of this release.
     * 
     * @return the name of this release.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
    public String getName() throws IllegalAccessException {
        return getString("name", false);
    }

    /**
     * Returns the creation date of this release.
     * 
     * @return the creation date of this release.
     * @throws IllegalAccessException
     * @see #getPublicationDate()
     */
    @GitHubAccessPoint(path = "@created_at", type = Date.class, requiresAccessToken = false)
    public Date getCreationDate() throws IllegalAccessException {
        return getDate("created_at", false);
    }

    /**
     * Returns the publication date of this release.
     * 
     * @return the publication date of this release.
     * @throws IllegalAccessException
     * @see #getCreationDate()
     */
    @GitHubAccessPoint(path = "@published_at", type = Date.class, requiresAccessToken = false)
    public Date getPublicationDate() throws IllegalAccessException {
        return getDate("published_at", false);
    }

    /**
     * Returns the content of this release's notes.
     * 
     * @return the content of this release's notes.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@body", type = String.class, requiresAccessToken = false)
    public String getBody() throws IllegalAccessException {
        return getString("body", false);
    }

    /**
     * Returns the author of this release.
     * 
     * @return the author of this release.
     * @throws IllegalAccessException
     */
    @GitHubAccessPoint(path = "@author", type = GitHubUser.class, requiresAccessToken = false)
    public GitHubUser getAuthor() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "author") ? null : new GitHubUser(api, response.get("author").getAsJsonObject().get("login").getAsString(), response.get("author").getAsJsonObject());
    }
}
