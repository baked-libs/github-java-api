package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GitHubGist extends GitHubObject {
	
	public GitHubGist(GitHubWebAPI api, String id) {
		super(api, null, "gists/" + id);
	}
	
	public GitHubGist(GitHubWebAPI api, String id, JsonElement response) {
		super(api, null, "gists/" + id);
		
		this.minimal = response;
	}
	
	public String getID() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "id") ? null: response.get("id").getAsString();
	}
	
	public Date getCreationDate() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "created_at") ? null: GitHubDate.parse(response.get("created_at").getAsString());
	}
	
	public Date getLastUpdatedDate() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "updated_at") ? null: GitHubDate.parse(response.get("updated_at").getAsString());
	}

}
