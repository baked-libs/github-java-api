package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;

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

	public int getSize() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, getName()) ? null: response.get(getName()).getAsInt();
	}

}
