package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubUser extends UniqueGitHubObject {
	
	private String username;

	public GitHubUser(GitHubWebAPI api, String username) {
		super(api, null, "users/" + username);
		
		this.username = username;
	}

	public GitHubUser(GitHubWebAPI api, String username, JsonElement response) {
		super(api, null, "users/" + username);
		
		this.username = username;
		this.minimal = response;
	}

	public GitHubUser(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*users/.*";
	}
	
	public GitHubRepository getRepository(String repo) {
		return new GitHubRepository(this.api, username, repo);
	}

	@GitHubAccessPoint(path = "/repos", type = GitHubRepository.class)
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

	@GitHubAccessPoint(path = "/starred", type = GitHubRepository.class)
	public List<GitHubRepository> getStarredRepositories() throws IllegalAccessException {
		GitHubObject repos = new GitHubObject(api, this, "/starred");
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

	@GitHubAccessPoint(path = "/subscriptions", type = GitHubRepository.class)
	public List<GitHubRepository> getSubscribedRepositories() throws IllegalAccessException {
		GitHubObject repos = new GitHubObject(api, this, "/subscriptions");
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

	@GitHubAccessPoint(path = "/followers", type = GitHubUser.class)
	public List<GitHubUser> getFollowers() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/followers");
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

	@GitHubAccessPoint(path = "/following", type = GitHubUser.class)
	public List<GitHubUser> getFollowing() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/following");
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

	@GitHubAccessPoint(path = "/orgs", type = GitHubOrganization.class)
	public List<GitHubOrganization> getOrganizations() throws IllegalAccessException {
		GitHubObject orgs = new GitHubObject(api, this, "/orgs");
		JsonElement response = orgs.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubOrganization> list = new ArrayList<GitHubOrganization>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubOrganization org = new GitHubOrganization(api, object.get("login").getAsString());
	    	list.add(org);
	    }
		
		return list;
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

	@GitHubAccessPoint(path = "/gists", type = GitHubGist.class)
	public List<GitHubGist> getPublicGists() throws IllegalAccessException {
		GitHubObject gists = new GitHubObject(api, this, "/gists");
		JsonElement response = gists.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubGist> list = new ArrayList<GitHubGist>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubGist org = new GitHubGist(api, object.get("id").getAsString(), object);
	    	list.add(org);
	    }
		
		return list;
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

	@GitHubAccessPoint(path = "@blog", type = String.class)
	public String getWebsite() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "blog") ? null: response.get("blog").getAsString();
	}

	@GitHubAccessPoint(path = "@company", type = String.class)
	public String getCompany() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "company") ? null: response.get("company").getAsString();
	}

	@GitHubAccessPoint(path = "@location", type = String.class)
	public String getLocation() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "location") ? null: response.get("location").getAsString();
	}

	@GitHubAccessPoint(path = "@email", type = String.class)
	public String getEmailAdress() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "email") ? null: response.get("email").getAsString();
	}

	@GitHubAccessPoint(path = "@hireable", type = Boolean.class)
	public boolean isHireable() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "hireable") ? false: response.get("hireable").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@site_admin", type = Boolean.class)
	public boolean isAdmin() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "site_admin") ? false: response.get("site_admin").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@bio", type = String.class)
	public String getDescription() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "bio") ? null: response.get("bio").getAsString();
	}

	@GitHubAccessPoint(path = "@avatar_url", type = String.class)
	public String getAvatarURL() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "avatar_url") ? null: response.get("avatar_url").getAsString();
	}

	@GitHubAccessPoint(path = "@public_repos", type = Integer.class)
	public int getRepositoriesAmount() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "public_repos") ? null: response.get("public_repos").getAsInt();
	}

	@GitHubAccessPoint(path = "@public_gists", type = Integer.class)
	public int getGistsAmount() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "public_gists") ? null: response.get("public_gists").getAsInt();
	}

	@GitHubAccessPoint(path = "@followers", type = Integer.class)
	public int getFollowersAmount() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "followers") ? null: response.get("followers").getAsInt();
	}

	@GitHubAccessPoint(path = "@following", type = Integer.class)
	public int getFollowingAmount() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "following") ? null: response.get("following").getAsInt();
	}

}
