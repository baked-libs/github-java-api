package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubBranch extends GitHubObject {
	
	private GitHubRepository repo;
	private String name;
	
	public GitHubBranch(GitHubWebAPI api, GitHubRepository repo, String name) {
		super(api, repo, "/branches/" + name);
		
		this.name = name;
		this.repo = repo;
	}
	
	public GitHubBranch(GitHubWebAPI api, GitHubRepository repo, String name, JsonElement response) {
		super(api, repo, "/branches/" + name);

		this.name = name;
		this.repo = repo;
		this.minimal = response;
	}

	public GitHubBranch(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	@GitHubAccessPoint(path = "@_links/self", type = String.class)
	public String getRawURL() {
		return ".*repos/.*/.*/branches/.*";
	}
	
	@GitHubAccessPoint(path = "@name", type = String.class)
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@commit", type = GitHubCommit.class)
	public GitHubCommit getLastCommit() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		if (!isInvalid(response, "commit")) {
			if (!isInvalid(response.getAsJsonObject().get("commit").getAsJsonObject(), "sha")) {
				return new GitHubCommit(api, getRepository(), response.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString(), response.getAsJsonObject().get("commit").getAsJsonObject());
			}
		}
		
		return null;
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}
	
	public boolean isDefaultBranch() throws IllegalAccessException {
		return name.equals(getRepository().getDefaultBranch().name);
	}

	@GitHubAccessPoint(path = "@_links/self", type = String.class)
	@Override
	public String getURL() {
		return super.getURL();
	}
}
