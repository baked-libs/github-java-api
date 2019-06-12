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
		
		List<GitHubRepository> list = new ArrayList<>();
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
		
		List<GitHubUser> list = new ArrayList<>();
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
		return getString("name", true);
	}

	@GitHubAccessPoint(path = "@login", type = String.class, requiresAccessToken = false)
	public String getUsername() throws IllegalAccessException {
		return getString("login", false);
	}

	@GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
	public String getDescription() throws IllegalAccessException {
		return getString("description", false);
	}

	@GitHubAccessPoint(path = "@avatar_url", type = String.class, requiresAccessToken = false)
	public String getAvatarURL() throws IllegalAccessException {
		return getString("avatar_url", false);
	}

	@GitHubAccessPoint(path = "@type", type = String.class, requiresAccessToken = false)
	public String getType() throws IllegalAccessException {
		return getString("type", false);
	}

	@GitHubAccessPoint(path = "@blog", type = String.class, requiresAccessToken = false)
	public String getWebsite() throws IllegalAccessException {
		return getString("blog", true);
	}

	@GitHubAccessPoint(path = "@company", type = String.class, requiresAccessToken = false)
	public String getCompany() throws IllegalAccessException {
		return getString("company", true);
	}

	@GitHubAccessPoint(path = "@location", type = String.class, requiresAccessToken = false)
	public String getLocation() throws IllegalAccessException {
		return getString("location", true);
	}

	@GitHubAccessPoint(path = "@email", type = String.class, requiresAccessToken = false)
	public String getEmailAdress() throws IllegalAccessException {
		return getString("email", true);
	}

}
