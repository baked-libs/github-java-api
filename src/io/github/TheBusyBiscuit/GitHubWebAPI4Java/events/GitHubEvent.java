package io.github.TheBusyBiscuit.GitHubWebAPI4Java.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubUser;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubEvent extends GitHubObject {
	
	public GitHubEvent(GitHubWebAPI api, GitHubUser user, String suffix, JsonElement response) {
		super(api, user, suffix);
		
		this.minimal = response;
	}

	public GitHubEvent(GitHubObject obj) {
		super(obj);
	}

	@GitHubAccessPoint(path = "@id", type = Integer.class, requiresAccessToken = false)
	public int getID() throws IllegalAccessException {
		JsonObject response = (JsonObject) getResponse(false);
		return isInvalid(response, "id") ? null: response.get("id").getAsInt();
	}

	@GitHubAccessPoint(path = "@type", type = String.class, requiresAccessToken = false)
	public String getType() throws IllegalAccessException {
		JsonObject response = (JsonObject) getResponse(false);
		return isInvalid(response, "type") ? null: response.get("type").getAsString();
	}
}