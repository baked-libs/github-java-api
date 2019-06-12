package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.UniqueGitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubRepository;

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

	@GitHubAccessPoint(path = "/repos", type = GitHubRepository.class, requiresAccessToken = false)
	public List<GitHubRepository> getRepositories() throws IllegalAccessException {
		GitHubObject repos = new GitHubObject(api, this, "/repos");
		JsonElement response = repos.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubRepository> list = new ArrayList<GitHubRepository>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubRepository repo = new GitHubRepository(api, object.get("full_name").getAsString(), object);
	    	list.add(repo);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/members", type = GitHubUser.class, requiresAccessToken = false)
	public List<GitHubUser> getMembers() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/members");
		JsonElement response = users.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubUser> list = new ArrayList<GitHubUser>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubUser user = new GitHubUser(api, object.get("login").getAsString(), object);
	    	list.add(user);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@login", type = String.class, requiresAccessToken = false)
	public String getUsername() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "login") ? null: response.get("login").getAsString();
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

	@GitHubAccessPoint(path = "@avatar_url", type = String.class, requiresAccessToken = false)
	public String getAvatarURL() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "avatar_url") ? null: response.get("avatar_url").getAsString();
	}

	@GitHubAccessPoint(path = "@type", type = String.class, requiresAccessToken = false)
	public String getType() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "type") ? null: response.get("type").getAsString();
	}

	@GitHubAccessPoint(path = "@blog", type = String.class, requiresAccessToken = false)
	public String getWebsite() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "blog") ? null: response.get("blog").getAsString();
	}

	@GitHubAccessPoint(path = "@company", type = String.class, requiresAccessToken = false)
	public String getCompany() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "company") ? null: response.get("company").getAsString();
	}

	@GitHubAccessPoint(path = "@location", type = String.class, requiresAccessToken = false)
	public String getLocation() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "location") ? null: response.get("location").getAsString();
	}

	@GitHubAccessPoint(path = "@email", type = String.class, requiresAccessToken = false)
	public String getEmailAdress() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "email") ? null: response.get("email").getAsString();
	}

}
