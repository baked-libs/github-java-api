package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import com.google.gson.JsonElement;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;

public class GitHubBlob extends GitHubFile {

    public GitHubBlob(GitHubWebAPI api, GitHubRepository repo, String id) {
        super(api, repo, "/git/blobs/" + id);
    }

    public GitHubBlob(GitHubWebAPI api, GitHubRepository repo, String id, JsonElement response) {
        super(api, repo, "/git/blobs/" + id, response);
    }

    public GitHubBlob(GitHubObject obj) {
        super(obj);
    }

    @Override
    public String getRawURL() {
        return ".*repos/.*/.*/git/blobs/.*";
    }

    @Override
    @GitHubAccessPoint(path = "@size", type = Integer.class, requiresAccessToken = false)
    public Integer getSize() throws IllegalAccessException {
        return getInteger("size", false);
    }

    @GitHubAccessPoint(path = "@content", type = String.class, requiresAccessToken = false)
    public String getFileContent() throws IllegalAccessException {
        return getString("content", true);
    }

    @GitHubAccessPoint(path = "@encoding", type = String.class, requiresAccessToken = false)
    public String getEncoding() throws IllegalAccessException {
        return getString("encoding", true);
    }
}
