package io.github.thebusybiscuit.githubjavaapi.objects.repositories;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;

public class GitHubLanguage extends GitHubObject {

    private String name;
    private GitHubRepository repo;

    public GitHubLanguage(GitHubWebAPI api, GitHubRepository repo, String name) {
        super(api, repo, "/languages");

        this.name = name;
        this.repo = repo;
    }

    public GitHubLanguage(GitHubWebAPI api, GitHubRepository repo, String name, JsonElement response) {
        super(api, repo, "/languages");

        this.name = name;
        this.repo = repo;

        this.minimal = response;
    }

    public GitHubRepository getRepository() {
        return this.repo;
    }

    @Override
    public String getRawURL() {
        return ".*repos/.*/.*/languages";
    }

    public String getName() {
        return this.name;
    }

    public Integer getSize() throws IllegalAccessException {
        return getInteger(getName(), false);
    }

}
