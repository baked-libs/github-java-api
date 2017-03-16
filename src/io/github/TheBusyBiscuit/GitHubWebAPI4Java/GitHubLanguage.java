package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GitHubLanguage extends GitHubObject {

	private String name;
	private GitHubRepository repo;
	
	public GitHubLanguage(GitHubWebAPI api, GitHubRepository repo, String name) {
		super(api, repo, "/languages");
		
		this.name = name;
		this.repo = repo;
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
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, getName()) ? null: response.get(getName()).getAsInt();
	}

}
