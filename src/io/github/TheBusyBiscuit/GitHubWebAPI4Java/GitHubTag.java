package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubTag extends GitHubObject {
	
	GitHubRepository repo;
	
	public GitHubTag(GitHubWebAPI api, GitHubRepository repo) {
		super(api, repo, "/tags");
		
		this.repo = repo;
	}
	
	public GitHubTag(GitHubWebAPI api, GitHubRepository repo, JsonElement response) {
		super(api, repo, "/tags");
		
		this.repo = repo;
		this.minimal = response;
	}

	public GitHubTag(GitHubObject obj) {
		super(obj);
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/tags.*";
	}

	@GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
	public String getName() throws IllegalAccessException {
		if (minimal == null) {
			throw new IllegalAccessException("Invalid GitHubTag Instance.");
		}
		
		JsonObject response = minimal.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@commit", type = GitHubCommit.class, requiresAccessToken = false)
	public GitHubCommit getCommit() throws IllegalAccessException {
		if (minimal == null) {
			throw new IllegalAccessException("Invalid GitHubTag Instance.");
		}
		
		JsonObject response = minimal.getAsJsonObject().get("commit").getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: new GitHubCommit(api, getRepository(), response.get("sha").getAsString(), response);
	}
}
