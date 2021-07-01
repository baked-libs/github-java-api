package io.github.thebusybiscuit.githubjavaapi.objects.repositories;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.githubjavaapi.GitHubWebAPI;
import io.github.thebusybiscuit.githubjavaapi.annotations.GitHubAccessPoint;
import io.github.thebusybiscuit.githubjavaapi.objects.GitHubObject;
import io.github.thebusybiscuit.githubjavaapi.objects.RepositoryFeature;
import io.github.thebusybiscuit.githubjavaapi.objects.users.GitHubUser;

public class GitHubPullRequest extends RepositoryFeature {

    GitHubRepository repo;

    public GitHubPullRequest(GitHubWebAPI api, GitHubRepository repo, int id) {
        super(api, repo, "/pulls/" + id);

        this.repo = repo;
    }

    public GitHubPullRequest(GitHubWebAPI api, GitHubRepository repo, int id, JsonElement response) {
        super(api, repo, "/pulls/" + id);

        this.repo = repo;
        this.minimal = response;
    }

    public GitHubPullRequest(GitHubObject obj) {
        super(obj);
    }

    public GitHubRepository getRepository() {
        return this.repo;
    }

    @Override
    @GitHubAccessPoint(path = "@_links/self/href", type = String.class, requiresAccessToken = false)
    public String getRawURL() {
        return ".*repos/.*/.*/pulls/.*";
    }

    @GitHubAccessPoint(path = "@user", type = GitHubUser.class, requiresAccessToken = false)
    public GitHubUser getUser() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "user") ? null : new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@locked", type = Boolean.class, requiresAccessToken = false)
    public Boolean isLocked() throws IllegalAccessException {
        return getBoolean("locked", false);
    }

    @GitHubAccessPoint(path = "@base/label", type = String.class, requiresAccessToken = false)
    public String getBaseLabel() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();

        return isInvalid(response, "label") ? null : response.get("label").getAsString();
    }

    @GitHubAccessPoint(path = "@base/user", type = GitHubUser.class, requiresAccessToken = false)
    public GitHubUser getBaseUser() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();

        return isInvalid(response, "user") ? null : new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@base/repo", type = GitHubRepository.class, requiresAccessToken = false)
    public GitHubRepository getBaseRepository() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();

        return isInvalid(response, "repo") ? null : new GitHubRepository(api, response.get("repo").getAsJsonObject().get("full_name").getAsString(), response.get("repo").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@base/ref", type = GitHubBranch.class, requiresAccessToken = false)
    public GitHubBranch getBaseBranch() throws IllegalAccessException, UnsupportedEncodingException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();

        return isInvalid(response, "ref") ? null : new GitHubBranch(api, getBaseRepository(), response.get("ref").getAsString());
    }

    @GitHubAccessPoint(path = "@base/sha", type = GitHubCommit.class, requiresAccessToken = false)
    public GitHubCommit getBaseCommit() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();

        return isInvalid(response, "sha") ? null : new GitHubCommit(api, getBaseRepository(), response.get("sha").getAsString());
    }

    @GitHubAccessPoint(path = "@head/label", type = String.class, requiresAccessToken = false)
    public String getHeadLabel() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();

        return isInvalid(response, "label") ? null : response.get("label").getAsString();
    }

    @GitHubAccessPoint(path = "@head/user", type = GitHubUser.class, requiresAccessToken = false)
    public GitHubUser getHeadUser() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();

        return isInvalid(response, "user") ? null : new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@head/repo", type = GitHubRepository.class, requiresAccessToken = false)
    public GitHubRepository getHeadRepository() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();

        return isInvalid(response, "repo") ? null : new GitHubRepository(api, response.get("repo").getAsJsonObject().get("full_name").getAsString(), response.get("repo").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@head/ref", type = GitHubBranch.class, requiresAccessToken = false)
    public GitHubBranch getHeadBranch() throws IllegalAccessException, UnsupportedEncodingException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();

        return isInvalid(response, "ref") ? null : new GitHubBranch(api, getHeadRepository(), response.get("ref").getAsString());
    }

    @GitHubAccessPoint(path = "@head/sha", type = GitHubCommit.class, requiresAccessToken = false)
    public GitHubCommit getHeadCommit() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();

        return isInvalid(response, "sha") ? null : new GitHubCommit(api, getHeadRepository(), response.get("sha").getAsString());
    }

    @GitHubAccessPoint(path = "@_links/self/commits", type = GitHubCommit.class, requiresAccessToken = false)
    public List<GitHubCommit> getCommits() throws IllegalAccessException {
        return this.getCommits(1);
    }

    @GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
    public List<GitHubCommit> getAllCommits() throws IllegalAccessException {
        List<GitHubCommit> commits = new ArrayList<GitHubCommit>();

        int i = 2;
        List<GitHubCommit> temp = getCommits(1);

        while (!temp.isEmpty()) {
            commits.addAll(temp);

            temp = getCommits(i);
            i++;
        }

        return commits;
    }

    @GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
    public List<GitHubCommit> getCommits(int page) throws IllegalAccessException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("page", String.valueOf(page));
        params.put("per_page", "100");

        GitHubObject commits = new GitHubObject(api, this, "/commits") {

            @Override
            public Map<String, String> getParameters() {
                return params;
            }

        };
        JsonElement response = commits.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubCommit> list = new ArrayList<GitHubCommit>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubCommit commit = new GitHubCommit(api, getHeadRepository(), object.get("sha").getAsString(), object);
            list.add(commit);
        }

        return list;
    }

    @GitHubAccessPoint(path = "@changed_files", type = Integer.class, requiresAccessToken = false)
    public Integer getFileChanges() throws IllegalAccessException {
        return getInteger("changed_files", true);
    }

    @GitHubAccessPoint(path = "@additions", type = Integer.class, requiresAccessToken = false)
    public Integer getAdditions() throws IllegalAccessException {
        return getInteger("additions", true);
    }

    @GitHubAccessPoint(path = "@deletions", type = Integer.class, requiresAccessToken = false)
    public Integer getDeletions() throws IllegalAccessException {
        return getInteger("deletions", true);
    }

    @GitHubAccessPoint(path = "@commits", type = Integer.class, requiresAccessToken = false)
    public Integer getCommitsAmount() throws IllegalAccessException {
        return getInteger("commits", true);
    }

    @GitHubAccessPoint(path = "@merged_by", type = GitHubUser.class, requiresAccessToken = false)
    public GitHubUser getMergedBy() throws IllegalAccessException {
        JsonElement element = getResponse(true);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "merged_by") ? null : new GitHubUser(api, response.get("merged_by").getAsJsonObject().get("login").getAsString(), response.get("closed_by").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@milestone", type = GitHubMilestone.class, requiresAccessToken = false)
    public GitHubMilestone getMilestone() throws IllegalAccessException {
        JsonElement element = getResponse(true);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "milestone") ? null : new GitHubMilestone(api, getRepository(), response.get("milestone").getAsJsonObject().get("number").getAsInt(), response.get("milestone").getAsJsonObject());
    }

    @GitHubAccessPoint(path = "@merged", type = Boolean.class, requiresAccessToken = false)
    public Boolean isMerged() throws IllegalAccessException {
        return getBoolean("merged", true);
    }

    @GitHubAccessPoint(path = "@mergeable", type = Boolean.class, requiresAccessToken = false)
    public Boolean isMergeable() throws IllegalAccessException {
        return getBoolean("mergeable", true);
    }

    @GitHubAccessPoint(path = "@body", type = String.class, requiresAccessToken = false)
    public String getMessageBody() throws IllegalAccessException {
        return getString("body", false);
    }

    @GitHubAccessPoint(path = "@merged_at", type = Date.class, requiresAccessToken = false)
    public Date getMergedDate() throws IllegalAccessException {
        return getDate("merged_at", true);
    }

    @GitHubAccessPoint(path = "@issue_url", type = GitHubIssue.class, requiresAccessToken = false)
    public GitHubIssue toIssue() throws IllegalAccessException {
        return new GitHubIssue(api, getRepository(), getNumber());
    }

    @GitHubAccessPoint(path = "@assignees", type = GitHubUser.class, requiresAccessToken = false)
    public List<GitHubUser> getAssignees() throws IllegalAccessException, UnsupportedEncodingException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        List<GitHubUser> users = new ArrayList<>();

        JsonArray array = response.get("assignees").getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            users.add(new GitHubUser(api, obj.get("login").getAsString(), obj));
        }

        return users;
    }

    @GitHubAccessPoint(path = "@merge_commit_sha", type = GitHubCommit.class, requiresAccessToken = false)
    public GitHubCommit getMergeCommit() throws IllegalAccessException {
        JsonElement element = getResponse(false);

        if (element == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }
        JsonObject response = element.getAsJsonObject();

        return isInvalid(response, "merge_commit_sha") ? null : new GitHubCommit(api, getBaseRepository(), response.get("merge_commit_sha").getAsString());
    }

    @GitHubAccessPoint(path = "/comments", type = GitHubComment.class, requiresAccessToken = false)
    public List<GitHubComment> getComments() throws IllegalAccessException {
        GitHubObject repos = new GitHubObject(api, this, "/comments");
        JsonElement response = repos.getResponse(true);

        if (response == null) {
            throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
        }

        List<GitHubComment> list = new ArrayList<>();
        JsonArray array = response.getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            GitHubComment comment = new GitHubComment(api, getRepository(), object.get("id").getAsInt(), object);
            list.add(comment);
        }

        return list;
    }

    public GitHubComment getComment(int id) throws IllegalAccessException {
        return new GitHubComment(api, getRepository(), id);
    }

    @GitHubAccessPoint(path = "@comments", type = Integer.class, requiresAccessToken = false)
    public Integer getCommentsAmount() throws IllegalAccessException {
        return getInteger("comments", false);
    }
}
