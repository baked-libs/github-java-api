package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubCommit extends GitHubObject {
	
	GitHubRepository repo;
	
	public GitHubCommit(GitHubWebAPI api, GitHubRepository repo, String id, JsonElement response) {
		super(api, repo, "/commits/" + id);
		
		this.repo = repo;
		this.minimal = response;
	}

	public GitHubCommit(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/commits/.*";
	}

	@GitHubAccessPoint(path = "@sha", type = String.class)
	public String getID() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: response.get("sha").getAsString();
	}

	@GitHubAccessPoint(path = "@author", type = GitHubUser.class)
	public GitHubUser getAuthor() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "author") ? null: new GitHubUser(api, response.get("author").getAsJsonObject().get("login").getAsString(), response.get("author").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@committer", type = GitHubUser.class)
	public GitHubUser getCommitter() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "committer") ? null: new GitHubUser(api, response.get("committer").getAsJsonObject().get("login").getAsString(), response.get("committer").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@stats/total", type = Integer.class)
	public int getTotalModifications() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("stats").getAsJsonObject();

		return isInvalid(response, "total") ? null: response.get("total").getAsInt();
	}

	@GitHubAccessPoint(path = "@stats/additions", type = Integer.class)
	public int getAdditions() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("stats").getAsJsonObject();

		return isInvalid(response, "additions") ? null: response.get("additions").getAsInt();
	}

	@GitHubAccessPoint(path = "@stats/deletions", type = Integer.class)
	public int getDeletions() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("stats").getAsJsonObject();

		return isInvalid(response, "deletions") ? null: response.get("deletions").getAsInt();
	}

	@GitHubAccessPoint(path = "@parents", type = GitHubCommit.class)
	public List<GitHubCommit> getParents() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		List<GitHubCommit> parents = new ArrayList<GitHubCommit>();
		
		JsonArray array = response.get("parents").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.get(i).getAsJsonObject();
			parents.add(new GitHubCommit(api, repo, obj.get("sha").getAsString(), obj));
		}
		
		return parents;
	}

	@GitHubAccessPoint(path = "@commit/message", type = String.class)
	public String getMessage() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("commit").getAsJsonObject();
		
		return isInvalid(response, "message") ? null: response.get("message").getAsString();
	}

	@GitHubAccessPoint(path = "@commit/tree", type = GitHubFileTree.class)
	public GitHubFileTree getFileTree() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("commit").getAsJsonObject().get("tree").getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: new GitHubFileTree(api, repo, response.get("sha").getAsString(), true);
	}

}
