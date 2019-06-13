package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra.GitHubDate;

public class UniqueGitHubObject extends GitHubObject {

	public UniqueGitHubObject(GitHubWebAPI api, GitHubObject parent, String suffix) {
		super(api, parent, suffix);
	}

	public UniqueGitHubObject(GitHubObject obj) {
		super(obj);
	}

	@GitHubAccessPoint(path = "@id", type = Integer.class, requiresAccessToken = false)
	public int getID() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "id") ? null: response.get("id").getAsInt();
	}

	@GitHubAccessPoint(path = "@created_at", type = Date.class, requiresAccessToken = false)
	public Date getCreationDate() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "created_at") ? null: GitHubDate.parse(response.get("created_at").getAsString());
	}

	@GitHubAccessPoint(path = "@updated_at", type = Date.class, requiresAccessToken = false)
	public Date getLastUpdatedDate() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "updated_at") ? null: GitHubDate.parse(response.get("updated_at").getAsString());
	}

}
