package io.github.thebusybiscuit.githubjavaapi.objects.users;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubGist;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.UniqueGitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.repositories.GitHubRepository;

public class GitHubUser extends UniqueGitHubObject {

    private String username;

    public GitHubUser(GitHubWebAPI api, String username) {
        super(api, null, "users/" + username);

        this.username = username;
    }

    public GitHubUser(GitHubWebAPI api, String username, JsonElement response) {
        super(api, null, "users/" + username);

        this.username = username;
        this.minimal = response;
    }

    public GitHubUser(GitHubObject obj) {
        super(obj);
    }

    @Override
    public String getRawURL() {
        return ".*users/.*";
    }

    public GitHubRepository getRepository(String repo) {
        return new GitHubRepository(this.api, username, repo);
    }

    @GitHubAccessPoint(path = "/repos", type = GitHubRepository.class, requiresAccessToken = false)
    public List<GitHubRepository> getRepositories() throws IllegalAccessException {
        GitHubObject repos = new GitHubObject(api, this, "/repos");
        JsonElement response = repos.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubRepository> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubRepository repo = new GitHubRepository(api, object.get("full_name").getAsString(), object);
            list.add(repo);
        }

        return list;
    }

    @GitHubAccessPoint(path = "/starred", type = GitHubRepository.class, requiresAccessToken = false)
    public List<GitHubRepository> getStarredRepositories() throws IllegalAccessException {
        GitHubObject repos = new GitHubObject(api, this, "/starred");
        JsonElement response = repos.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubRepository> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubRepository repo = new GitHubRepository(api, object.get("full_name").getAsString(), object);
            list.add(repo);
        }

        return list;
    }

    @GitHubAccessPoint(path = "/subscriptions", type = GitHubRepository.class, requiresAccessToken = false)
    public List<GitHubRepository> getSubscribedRepositories() throws IllegalAccessException {
        GitHubObject repos = new GitHubObject(api, this, "/subscriptions");
        JsonElement response = repos.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubRepository> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubRepository repo = new GitHubRepository(api, object.get("full_name").getAsString(), object);
            list.add(repo);
        }

        return list;
    }

    @GitHubAccessPoint(path = "/followers", type = GitHubUser.class, requiresAccessToken = false)
    public List<GitHubUser> getFollowers() throws IllegalAccessException {
        GitHubObject users = new GitHubObject(api, this, "/followers");
        JsonElement response = users.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubUser> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubUser user = new GitHubUser(api, object.get("login").getAsString(), object);
            list.add(user);
        }

        return list;
    }

    @GitHubAccessPoint(path = "/following", type = GitHubUser.class, requiresAccessToken = false)
    public List<GitHubUser> getFollowing() throws IllegalAccessException {
        GitHubObject users = new GitHubObject(api, this, "/following");
        JsonElement response = users.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubUser> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubUser user = new GitHubUser(api, object.get("login").getAsString(), object);
            list.add(user);
        }

        return list;
    }

    @GitHubAccessPoint(path = "/orgs", type = GitHubOrganization.class, requiresAccessToken = false)
    public List<GitHubOrganization> getOrganizations() throws IllegalAccessException {
        GitHubObject orgs = new GitHubObject(api, this, "/orgs");
        JsonElement response = orgs.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubOrganization> list = new ArrayList<GitHubOrganization>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubOrganization org = new GitHubOrganization(api, object.get("login").getAsString());
            list.add(org);
        }

        return list;
    }

    @GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
    public String getName() throws IllegalAccessException {
        return getString("name", true);
    }

    @GitHubAccessPoint(path = "/gists", type = GitHubGist.class, requiresAccessToken = false)
    public List<GitHubGist> getPublicGists() throws IllegalAccessException {
        GitHubObject gists = new GitHubObject(api, this, "/gists");
        JsonElement response = gists.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubGist> list = new ArrayList<GitHubGist>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubGist org = new GitHubGist(api, object.get("id").getAsString(), object);
            list.add(org);
        }

        return list;
    }

    @GitHubAccessPoint(path = "@login", type = String.class, requiresAccessToken = false)
    public String getUsername() throws IllegalAccessException {
        if (this.username != null) {
            return this.username;
        }

        return getString("login", false);
    }

    @GitHubAccessPoint(path = "@blog", type = String.class, requiresAccessToken = false)
    public String getWebsite() throws IllegalAccessException {
        return getString("blog", true);
    }

    @GitHubAccessPoint(path = "@company", type = String.class, requiresAccessToken = false)
    public String getCompany() throws IllegalAccessException {
        return getString("company", true);
    }

    @GitHubAccessPoint(path = "@location", type = String.class, requiresAccessToken = false)
    public String getLocation() throws IllegalAccessException {
        return getString("location", true);
    }

    @GitHubAccessPoint(path = "@email", type = String.class, requiresAccessToken = false)
    public String getEmailAdress() throws IllegalAccessException {
        return getString("email", true);
    }

    @GitHubAccessPoint(path = "@hireable", type = Boolean.class, requiresAccessToken = false)
    public boolean isHireable() throws IllegalAccessException {
        return getBoolean("hireable", true);
    }

    @GitHubAccessPoint(path = "@site_admin", type = Boolean.class, requiresAccessToken = false)
    public boolean isAdmin() throws IllegalAccessException {
        return getBoolean("site_admin", false);
    }

    @GitHubAccessPoint(path = "@bio", type = String.class, requiresAccessToken = false)
    public String getDescription() throws IllegalAccessException {
        return getString("bio", true);
    }

    @GitHubAccessPoint(path = "@avatar_url", type = String.class, requiresAccessToken = false)
    public String getAvatarURL() throws IllegalAccessException {
        return getString("avatar_url", false);
    }

    @GitHubAccessPoint(path = "@type", type = String.class, requiresAccessToken = false)
    public String getType() throws IllegalAccessException {
        return getString("type", false);
    }

    @GitHubAccessPoint(path = "@public_repos", type = Integer.class, requiresAccessToken = false)
    public Integer getRepositoriesAmount() throws IllegalAccessException {
        return getInteger("public_repos", true);
    }

    @GitHubAccessPoint(path = "@public_gists", type = Integer.class, requiresAccessToken = false)
    public Integer getGistsAmount() throws IllegalAccessException {
        return getInteger("public_gists", true);
    }

    @GitHubAccessPoint(path = "@followers", type = Integer.class, requiresAccessToken = false)
    public Integer getFollowersAmount() throws IllegalAccessException {
        return getInteger("followers", true);
    }

    @GitHubAccessPoint(path = "@following", type = Integer.class, requiresAccessToken = false)
    public Integer getFollowingAmount() throws IllegalAccessException {
        return getInteger("following", true);
    }

    public GitHubOrganization toOrganization() throws IllegalAccessException {
        if (getType().equals("Organization")) {
            return new GitHubOrganization(api, username);
        } else {
            return null;
        }
    }

}
