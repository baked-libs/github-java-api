package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra.GitHubDate;

public class GitHubDownload extends GitHubObject {
	
	public GitHubDownload(GitHubWebAPI api, GitHubRepository repo, int id) {
		super(api, repo, "/downloads/" + id);
	}
	
	public GitHubDownload(GitHubWebAPI api, GitHubRepository repo, int id, JsonElement response) {
		super(api, repo, "/downloads/" + id);
		
		this.minimal = response;
	}

	public GitHubDownload(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/downloads/.*";
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
	
	@GitHubAccessPoint(path = "@size", type = Integer.class, requiresAccessToken = false)
	public int getSize() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "size") ? null: response.get("size").getAsInt();
	}
	
	@GitHubAccessPoint(path = "@download_count", type = Integer.class, requiresAccessToken = false)
	public int getDownloads() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "download_count") ? null: response.get("download_count").getAsInt();
	}

	@GitHubAccessPoint(path = "@content_type", type = String.class, requiresAccessToken = false)
	public String getContentType() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "content_type") ? null: response.get("content_type").getAsString();
	}

	@GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
	public String getDescription() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "description") ? null: response.get("description").getAsString();
	}
}
