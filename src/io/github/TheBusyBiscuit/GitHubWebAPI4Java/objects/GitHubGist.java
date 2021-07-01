package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubFile;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

public class GitHubGist extends GitHubObject {

    public GitHubGist(GitHubWebAPI api, String id) {
        super(api, null, "gists/" + id);
    }

    public GitHubGist(GitHubWebAPI api, String id, JsonElement response) {
        super(api, null, "gists/" + id);

        this.minimal = response;
    }

    public GitHubGist(GitHubObject obj) {
        super(obj);
    }

    @Override
    public String getRawURL() {
        return ".*gists/.*";
    }

    @GitHubAccessPoint(path = "@id", type = String.class, requiresAccessToken = false)
    public String getID() throws IllegalAccessException {
        return getString("id", false);
    }

    @GitHubAccessPoint(path = "@created_at", type = Date.class, requiresAccessToken = false)
    public Date getCreationDate() throws IllegalAccessException {
        return getDate("created_at", false);
    }

    @GitHubAccessPoint(path = "@updated_at", type = Date.class, requiresAccessToken = false)
    public Date getLastUpdatedDate() throws IllegalAccessException {
        return getDate("updated_at", false);
    }

    @GitHubAccessPoint(path = "@owner", type = GitHubUser.class, requiresAccessToken = false)
    public GitHubUser getOwner() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "owner") ? null : new GitHubUser(api, response.get("owner").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
    public String getDescription() throws IllegalAccessException {
        return getString("description", false);
    }

    /**
     * Returns a list of {@link GitHubFile} that are part of this gist.
     * 
     * @return list of {@link GitHubFile} that are part of this gist, may be empty.
     * @throws IllegalAccessException
     *             if no connection to the GitHub API could be established.
     * @since 1.3.3
     */
    public List<GitHubFile> getFiles() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        List<GitHubFile> files = new ArrayList<>();
        if (!isInvalid(response, "files")) {
            response.getAsJsonObject("files").keySet().forEach(fileName -> files.add(new GitHubFile(api, this, fileName)));
        }

        return files;
    }

    /**
     * Returns the {@link GitHubFile} representing the file with this name in this gist.
     * 
     * @param name
     *            name of the file to get, not null.
     * @return the {@link GitHubFile} representing the file with this name in this gist, or null if none could be found.
     * @since 1.3.3
     */
    public GitHubFile getFile(String name) throws IllegalAccessException {
        for (GitHubFile file : getFiles()) {
            if (name.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }
}
