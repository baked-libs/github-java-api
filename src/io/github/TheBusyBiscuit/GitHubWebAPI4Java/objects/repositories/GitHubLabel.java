package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;

public class GitHubLabel extends GitHubObject {
	
	private String param;
	private GitHubRepository repo;
	
	public GitHubLabel(GitHubWebAPI api, GitHubRepository repo, String name) throws UnsupportedEncodingException {
		super(api, repo, "/labels/" + URLEncoder.encode(name, "utf-8"));
		
		this.repo = repo;
		this.param = URLEncoder.encode(name, "utf-8");
	}
	
	public GitHubLabel(GitHubWebAPI api, GitHubRepository repo, String name, JsonElement response) throws UnsupportedEncodingException {
		super(api, repo, "/labels/" + URLEncoder.encode(name, "utf-8"));

		this.repo = repo;
		this.param = URLEncoder.encode(name, "utf-8");
		this.minimal = response;
	}

	public GitHubLabel(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/labels/.*";
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

	@GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@color", type = String.class, requiresAccessToken = false)
	public String getColor() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "color") ? null: response.get("color").getAsString();
	}

	@GitHubAccessPoint(path = "@default", type = Boolean.class, requiresAccessToken = false)
	public boolean isDefaultLabel() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "default") ? false: response.get("default").getAsBoolean();
	}
	
	public String getURLEncodedParameter() {
		return this.param;
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}

}
