package io.github.thebusybiscuit.githubjavaapi.objects.repositories;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.users.GitHubUser;

public class GitHubContributor extends GitHubUser {

    public GitHubContributor(GitHubWebAPI api, String username, JsonElement response) {
        super(api, username, response);
    }

    public GitHubContributor(GitHubObject obj) {
        super(obj);
    }

    @GitHubAccessPoint(path = "@contributions", type = Integer.class, requiresAccessToken = false)
    public Integer getContributionsAmount() throws IllegalAccessException {
        return getInteger("contributions", false);
    }

}
