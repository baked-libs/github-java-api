package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra.RepositorySnapshot;

public class GitHubCommit extends GitHubObject {
	
	GitHubRepository repo;
	
	public GitHubCommit(GitHubWebAPI api, GitHubRepository repo, String id) {
		super(api, repo, "/commits/" + id);
		
		this.repo = repo;
	}
	
	public GitHubCommit(GitHubWebAPI api, GitHubRepository repo, String id, JsonElement response) {
		super(api, repo, "/commits/" + id);
		
		this.repo = repo;
		this.minimal = response;
	}

	public GitHubCommit(GitHubObject obj) {
		super(obj);
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}
	
	@Override
	@GitHubAccessPoint(path = "@commit/url", type = String.class, requiresAccessToken = false)
	public String getRawURL() {
		return ".*repos/.*/.*/commits/.*";
	}

	@GitHubAccessPoint(path = "@sha", type = String.class, requiresAccessToken = false)
	public String getID() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: response.get("sha").getAsString();
	}

	@GitHubAccessPoint(path = "@author", type = GitHubUser.class, requiresAccessToken = false)
	public GitHubUser getAuthor() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "author") ? null: new GitHubUser(api, response.get("author").getAsJsonObject().get("login").getAsString(), response.get("author").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@committer", type = GitHubUser.class, requiresAccessToken = false)
	public GitHubUser getCommitter() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "committer") ? null: new GitHubUser(api, response.get("committer").getAsJsonObject().get("login").getAsString(), response.get("committer").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@stats/total", type = Integer.class, requiresAccessToken = false)
	public int getTotalChanges() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("stats").getAsJsonObject();

		return isInvalid(response, "total") ? null: response.get("total").getAsInt();
	}

	@GitHubAccessPoint(path = "@stats/additions", type = Integer.class, requiresAccessToken = false)
	public int getAdditions() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("stats").getAsJsonObject();

		return isInvalid(response, "additions") ? null: response.get("additions").getAsInt();
	}

	@GitHubAccessPoint(path = "@stats/deletions", type = Integer.class, requiresAccessToken = false)
	public int getDeletions() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("stats").getAsJsonObject();

		return isInvalid(response, "deletions") ? null: response.get("deletions").getAsInt();
	}

	@GitHubAccessPoint(path = "@parents", type = GitHubCommit.class, requiresAccessToken = false)
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
			parents.add(new GitHubCommit(api, repo, obj.get("sha").getAsString()));
		}
		
		return parents;
	}

	@GitHubAccessPoint(path = "@commit/message", type = String.class, requiresAccessToken = false)
	public String getMessage() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("commit").getAsJsonObject();
		
		return isInvalid(response, "message") ? null: response.get("message").getAsString();
	}

	@GitHubAccessPoint(path = "@commit/tree", type = GitHubFileTree.class, requiresAccessToken = false)
	public GitHubFileTree getFileTree() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("commit").getAsJsonObject().get("tree").getAsJsonObject();
		
		return isInvalid(response, "sha") ? null: new GitHubFileTree(api, repo, response.get("sha").getAsString(), true);
	}

	@GitHubAccessPoint(path = "@files", type = GitHubFileChange.class, requiresAccessToken = false)
	public List<GitHubFileChange> getFileChanges() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		JsonObject response = element.getAsJsonObject();
		
		List<GitHubFileChange> files = new ArrayList<GitHubFileChange>();
		
		JsonArray array = response.get("files").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.get(i).getAsJsonObject();
			JsonObject json = new JsonObject();
			json.addProperty("sha", obj.get("sha").getAsString());
			json.addProperty("type", "blob");
			json.addProperty("path", obj.get("filename").getAsString());
			files.add(new GitHubFileChange(api, getRepository(), obj, json));
		}
		
		return files;
	}

	@GitHubAccessPoint(path = "/comments", type = GitHubComment.class, requiresAccessToken = false)
	public List<GitHubComment> getComments() throws IllegalAccessException {
		GitHubObject repos = new GitHubObject(api, this, "/comments");
		JsonElement response = repos.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubComment> list = new ArrayList<GitHubComment>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubComment comment = new GitHubComment(api, getRepository(), object.get("id").getAsInt(), object);
	    	list.add(comment);
	    }
		
		return list;
	}

	public GitHubComment getComment(int id) throws IllegalAccessException {
		return new GitHubComment(api, getRepository(), id);
	}

	@GitHubAccessPoint(path = "@commit/comment_count", type = Integer.class, requiresAccessToken = false)
	public int getCommentsAmount() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject().get("commit").getAsJsonObject();

		return isInvalid(response, "comment_count") ? null: response.get("comment_count").getAsInt();
	}

	public RepositorySnapshot getSnapshot() {
		return new RepositorySnapshot(this);
	}
}
