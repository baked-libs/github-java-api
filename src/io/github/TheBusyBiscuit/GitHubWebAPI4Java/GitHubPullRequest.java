package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubPullRequest extends RepositoryFeature {
	
	GitHubRepository repo;
	
	public GitHubPullRequest(GitHubWebAPI api, GitHubRepository repo, int id, JsonElement response) {
		super(api, repo, "/pulls/" + id);
		
		this.repo = repo;
		this.minimal = response;
	}

	public GitHubPullRequest(GitHubObject obj) {
		super(obj);
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/pulls/.*";
	}
	
	@GitHubAccessPoint(path = "@base/user", type = GitHubUser.class)
	public GitHubUser getBaseUser() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();
		
		return isInvalid(response, "user") ? null: new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}
	
	@GitHubAccessPoint(path = "@base/repo", type = GitHubRepository.class)
	public GitHubRepository getBaseRepository() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();
		
		return isInvalid(response, "repo") ? null: new GitHubRepository(api, response.get("repo").getAsJsonObject().get("full_name").getAsString(), response.get("repo").getAsJsonObject());
	}
	
	@GitHubAccessPoint(path = "@base/ref", type = GitHubBranch.class)
	public GitHubBranch getBaseBranch() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();
		
		return isInvalid(response, "ref") ? null: new GitHubBranch(api, getBaseRepository(), response.get("ref").getAsString());
	}
	
	@GitHubAccessPoint(path = "@base/sha", type = GitHubCommit.class)
	public GitHubCommit getBaseCommit() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("base").getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: new GitHubCommit(api, getBaseRepository(), response.get("sha").getAsString());
	}
	
	@GitHubAccessPoint(path = "@head/user", type = GitHubUser.class)
	public GitHubUser getHeadUser() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();
		
		return isInvalid(response, "user") ? null: new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}
	
	@GitHubAccessPoint(path = "@head/repo", type = GitHubRepository.class)
	public GitHubRepository getHeadRepository() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();
		
		return isInvalid(response, "repo") ? null: new GitHubRepository(api, response.get("repo").getAsJsonObject().get("full_name").getAsString(), response.get("repo").getAsJsonObject());
	}
	
	@GitHubAccessPoint(path = "@head/ref", type = GitHubBranch.class)
	public GitHubBranch getHeadBranch() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();
		
		return isInvalid(response, "ref") ? null: new GitHubBranch(api, getHeadRepository(), response.get("ref").getAsString());
	}
	
	@GitHubAccessPoint(path = "@head/sha", type = GitHubCommit.class)
	public GitHubCommit getHeadCommit() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("head").getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: new GitHubCommit(api, getHeadRepository(), response.get("sha").getAsString());
	}

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class)
	public List<GitHubCommit> getCommits() throws IllegalAccessException {
		return this.getCommits(1);
	}

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class)
	public List<GitHubCommit> getAllCommits() throws IllegalAccessException {
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

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class)
	public List<GitHubCommit> getCommits(int page) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("page", String.valueOf(page));
		
		GitHubObject commits = new GitHubObject(api, this, "/commits") {
			
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
	    	
	    	GitHubCommit commit = new GitHubCommit(api, getHeadRepository(), object.get("sha").getAsString(), object);
	    	list.add(commit);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "@changed_files", type = Integer.class)
	public int getFileChanges() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "changed_files") ? null: response.get("changed_files").getAsInt();
	}

	@GitHubAccessPoint(path = "@additions", type = Integer.class)
	public int getAdditions() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "additions") ? null: response.get("additions").getAsInt();
	}

	@GitHubAccessPoint(path = "@deletions", type = Integer.class)
	public int getDeletions() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "deletions") ? null: response.get("deletions").getAsInt();
	}

	@GitHubAccessPoint(path = "@commits", type = Integer.class)
	public int getCommitsAmount() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "commits") ? null: response.get("commits").getAsInt();
	}

	@GitHubAccessPoint(path = "@merged_by", type = GitHubUser.class)
	public GitHubUser getMergedBy() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "merged_by") ? null: new GitHubUser(api, response.get("merged_by").getAsJsonObject().get("login").getAsString(), response.get("closed_by").getAsJsonObject());
	}
	
	public GitHubIssue toIssue() throws IllegalAccessException {
		return new GitHubIssue(api, getRepository(), getNumber());
	}
}
