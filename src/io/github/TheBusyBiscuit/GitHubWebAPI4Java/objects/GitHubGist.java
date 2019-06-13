package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

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

	@GitHubAccessPoint(path = "@id", type = String.class, requiresAccessToken = false)
	public String getID() throws IllegalAccessException {
		return getString("id", false);
	}

	@GitHubAccessPoint(path = "@created_at", type = Date.class, requiresAccessToken = false)
	public Date getCreationDate() throws IllegalAccessException {
		return getDate("created_at", false);
	}

	@GitHubAccessPoint(path = "@updated_at", type = Date.class, requiresAccessToken = false)
	public Date getLastUpdatedDate() throws IllegalAccessException {
		return getDate("updated_at", false);
	}

	@GitHubAccessPoint(path = "@owner", type = GitHubUser.class, requiresAccessToken = false)
	public GitHubUser getOwner() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "owner") ? null: new GitHubUser(api, response.get("owner").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
	public String getDescription() throws IllegalAccessException {
		return getString("description", false);
	}

}
