package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import com.google.gson.JsonElement;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

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
