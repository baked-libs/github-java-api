package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubFileChange extends GitHubBlob {
	
	private JsonObject obj = null;
	
	public GitHubFileChange(GitHubWebAPI api, GitHubRepository repo, JsonObject obj) {
		super(api, repo, obj.get("sha").getAsString());
		
		this.obj = obj;
	}
	
	public GitHubFileChange(GitHubWebAPI api, GitHubRepository repo, JsonObject obj, JsonElement response) {
		super(api, repo, obj.get("sha").getAsString(), response);
		
		this.obj = obj;
	}

	public GitHubFileChange(GitHubObject obj) {
		super(obj);
	}
	
	@GitHubAccessPoint(path = "@changes", type = Integer.class)
	public int getTotalChanges() throws IllegalAccessException {
		return isInvalid(obj, "changes") ? null: obj.get("changes").getAsInt();
	}

	@GitHubAccessPoint(path = "@additions", type = Integer.class)
	public int getAdditions() throws IllegalAccessException {
		return isInvalid(obj, "additions") ? null: obj.get("additions").getAsInt();
	}

	@GitHubAccessPoint(path = "@deletions", type = Integer.class)
	public int getDeletions() throws IllegalAccessException {
		return isInvalid(obj, "deletions") ? null: obj.get("deletions").getAsInt();
	}
	
	@GitHubAccessPoint(path = "@status", type = Status.class)
	public Status getStatus() throws IllegalAccessException {
		return isInvalid(obj, "status") ? null: Status.valueOf(obj.get("status").getAsString().toUpperCase());
	}
	
	@Override
	@GitHubAccessPoint(path = "@filename", type = String.class)
	public String getFile() throws IllegalAccessException {
		return isInvalid(obj, "filename") ? null: obj.get("filename").getAsString();
	}
	
	@GitHubAccessPoint(path = "@blob_url", type = GitHubBlob.class)
	public GitHubBlob getAsBlob() {
		return this;
	}
	
	public enum Status {
		
		REMOVED,
		ADDED,
		MODIFIED;
		
	}

}
