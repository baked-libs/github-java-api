package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GitHubBranch extends GitHubObject {
	
	private GitHubRepository repo;
	
	public GitHubBranch(GitHubWebAPI api, GitHubRepository repo, String name) {
		super(api, repo, "/branches/" + name);
		
		this.repo = repo;
	}
	
	public GitHubBranch(GitHubWebAPI api, GitHubRepository repo, String name, JsonElement response) {
		super(api, repo, "/branches/" + name);
		
		this.repo = repo;
		this.minimal = response;
	}
	
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}
	
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
		return getName().equals(getRepository().getDefaultBranch());
	}

}
