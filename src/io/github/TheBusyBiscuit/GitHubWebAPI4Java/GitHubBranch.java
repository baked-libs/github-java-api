package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubBranch extends GitHubObject {
	
	private GitHubRepository repo;
	private String name;
	
	public GitHubBranch(GitHubWebAPI api, GitHubRepository repo, String name) throws UnsupportedEncodingException {
		super(api, repo, "/branches/" + URLEncoder.encode(name, "utf-8"));
		
		this.name = name;
		this.repo = repo;
	}
	
	public GitHubBranch(GitHubWebAPI api, GitHubRepository repo, String name, JsonElement response) throws UnsupportedEncodingException {
		super(api, repo, "/branches/" + URLEncoder.encode(name, "utf-8"));

		this.name = name;
		this.repo = repo;
		this.minimal = response;
	}

	public GitHubBranch(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/branches/.*";
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

	@GitHubAccessPoint(path = "@commit", type = GitHubCommit.class, requiresAccessToken = false)
	public GitHubCommit getLastCommit() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		if (!isInvalid(response, "commit")) {
			if (!isInvalid(response.getAsJsonObject().get("commit").getAsJsonObject(), "sha")) {
				return new GitHubCommit(api, getRepository(), response.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString(), response.getAsJsonObject().get("commit").getAsJsonObject());
			}
		}
		
		return null;
	}

	@GitHubAccessPoint(path = "@commit/sha", type = String.class, requiresAccessToken = false)
	public String getLastCommitSHA() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		if (!isInvalid(response, "commit")) {
			if (!isInvalid(response.getAsJsonObject().get("commit").getAsJsonObject(), "sha")) {
				return response.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString();
			}
		}
		
		return null;
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}
	
	public boolean isDefaultBranch() throws IllegalAccessException, UnsupportedEncodingException {
		return name.equals(getRepository().getDefaultBranch().name);
	}

	@GitHubAccessPoint(path = "@_links/self", type = String.class, requiresAccessToken = false)
	@Override
	public String getURL() {
		return super.getURL();
	}

	public List<GitHubCommit> getCommits() throws IllegalAccessException, UnsupportedEncodingException {
		return this.getCommits(1);
	}

	public List<GitHubCommit> getAllCommits() throws IllegalAccessException, UnsupportedEncodingException {
		List<GitHubCommit> commits = new ArrayList<GitHubCommit>();
		
		int i = 2;
		List<GitHubCommit> temp = getCommits(1);
		
		while (!temp.isEmpty()) {
			commits.addAll(temp);
			
			temp = getCommits(i);
			i++;
		}
		
		return commits;
	}

	public GitHubCommit getCommit(String sha) throws IllegalAccessException {
		return new GitHubCommit(api, getRepository(), sha);
	}

	public List<GitHubCommit> getCommits(final int page) throws IllegalAccessException, UnsupportedEncodingException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sha", URLEncoder.encode(this.getName(), "utf-8"));
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(GitHubWebAPI.ITEMS_PER_PAGE));
		
		GitHubObject commits = new GitHubObject(api, getRepository(), "/commits") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		
		JsonElement response = commits.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubCommit> list = new ArrayList<GitHubCommit>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubCommit commit = new GitHubCommit(api, getRepository(), object.get("sha").getAsString(), object);
	    	list.add(commit);
	    }
		
		return list;
	}
}
