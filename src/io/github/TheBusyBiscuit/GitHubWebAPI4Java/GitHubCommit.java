package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GitHubCommit extends GitHubObject {
	
	public GitHubCommit(GitHubWebAPI api, GitHubRepository repo, String id, JsonElement response) {
		super(api, repo, "/commits/" + id);
		
		this.minimal = response;
	}
	
	public String getID() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: response.get("sha").getAsString();
	}

}
