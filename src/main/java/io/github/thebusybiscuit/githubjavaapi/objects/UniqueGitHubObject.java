package io.github.thebusybiscuit.githubjavaapi.objects;

import java.util.Date;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;

public class UniqueGitHubObject extends GitHubObject {

    public UniqueGitHubObject(GitHubWebAPI api, GitHubObject parent, String suffix) {
        super(api, parent, suffix);
    }

    public UniqueGitHubObject(GitHubObject obj) {
        super(obj);
    }

    @GitHubAccessPoint(path = "@id", type = Integer.class, requiresAccessToken = false)
    public int getID() throws IllegalAccessException {
        return getInteger("id", false);
    }

    @GitHubAccessPoint(path = "@created_at", type = Date.class, requiresAccessToken = false)
    public Date getCreationDate() throws IllegalAccessException {
        return getDate("created_at", true);
    }

    @GitHubAccessPoint(path = "@updated_at", type = Date.class, requiresAccessToken = false)
    public Date getLastUpdatedDate() throws IllegalAccessException {
        return getDate("updated_at", true);
    }

}
