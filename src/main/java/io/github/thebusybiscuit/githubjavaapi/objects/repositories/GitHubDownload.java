package io.github.thebusybiscuit.githubjavaapi.objects.repositories;

import java.util.Date;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;

public class GitHubDownload extends GitHubObject {

    public GitHubDownload(GitHubWebAPI api, GitHubRepository repo, int id) {
        super(api, repo, "/downloads/" + id);
    }

    public GitHubDownload(GitHubWebAPI api, GitHubRepository repo, int id, JsonElement response) {
        super(api, repo, "/downloads/" + id);

        this.minimal = response;
    }

    public GitHubDownload(GitHubObject obj) {
        super(obj);
    }

    @Override
    public String getRawURL() {
        return ".*repos/.*/.*/downloads/.*";
    }

    @GitHubAccessPoint(path = "@id", type = Integer.class, requiresAccessToken = false)
    public Integer getID() throws IllegalAccessException {
        return getInteger("id", false);
    }

    @GitHubAccessPoint(path = "@created_at", type = Date.class, requiresAccessToken = false)
    public Date getCreationDate() throws IllegalAccessException {
        return getDate("created_at", true);
    }

    @GitHubAccessPoint(path = "@size", type = Integer.class, requiresAccessToken = false)
    public Integer getSize() throws IllegalAccessException {
        return getInteger("size", false);
    }

    @GitHubAccessPoint(path = "@download_count", type = Integer.class, requiresAccessToken = false)
    public Integer getDownloads() throws IllegalAccessException {
        return getInteger("download_count", false);
    }

    @GitHubAccessPoint(path = "@content_type", type = String.class, requiresAccessToken = false)
    public String getContentType() throws IllegalAccessException {
        return getString("content_type", false);
    }

    @GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
    public String getName() throws IllegalAccessException {
        return getString("name", false);
    }

    @GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
    public String getDescription() throws IllegalAccessException {
        return getString("description", false);
    }
}
