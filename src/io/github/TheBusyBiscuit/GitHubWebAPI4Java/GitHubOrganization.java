package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubOrganization extends UniqueGitHubObject {
	
	public GitHubOrganization(GitHubWebAPI api, String name) {
		super(api, null, "orgs/" + name);
	}
	
	public GitHubOrganization(GitHubWebAPI api, String name, JsonElement response) {
		super(api, null, "orgs/" + name);
		
		this.minimal = response;
	}

	public GitHubOrganization(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*orgs/.*";
	}

	@GitHubAccessPoint(path = "@name", type = String.class)
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@login", type = String.class)
	public String getUsername() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "login") ? null: response.get("login").getAsString();
	}

	@GitHubAccessPoint(path = "@description", type = String.class)
	public String getDescription() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "description") ? null: response.get("description").getAsString();
	}

}
