package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubGist extends GitHubObject {
	
	public GitHubGist(GitHubWebAPI api, String id) {
		super(api, null, "gists/" + id);
	}
	
	public GitHubGist(GitHubWebAPI api, String id, JsonElement response) {
		super(api, null, "gists/" + id);
		
		this.minimal = response;
	}

	public GitHubGist(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*gists/.*";
	}

	@GitHubAccessPoint(path = "@id", type = String.class)
	public String getID() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "id") ? null: response.get("id").getAsString();
	}

	@GitHubAccessPoint(path = "@created_at", type = Date.class)
	public Date getCreationDate() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "created_at") ? null: GitHubDate.parse(response.get("created_at").getAsString());
	}

	@GitHubAccessPoint(path = "@updated_at", type = Date.class)
	public Date getLastUpdatedDate() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "updated_at") ? null: GitHubDate.parse(response.get("updated_at").getAsString());
	}

}
