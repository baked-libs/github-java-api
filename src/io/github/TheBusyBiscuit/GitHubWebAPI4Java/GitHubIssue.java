package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubIssue extends RepositoryFeature {
	
	private GitHubRepository repo;
	
	public GitHubIssue(GitHubWebAPI api, GitHubRepository repo, int number) {
		super(api, repo, "/issues/" + number);
		
		this.repo = repo;
	}
	
	public GitHubIssue(GitHubWebAPI api, GitHubRepository repo, int number, JsonElement response) {
		super(api, repo, "/issues/" + number);

		this.repo = repo;
		this.minimal = response;
	}

	public GitHubIssue(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/issues/.*";
	}

	@GitHubAccessPoint(path = "@user", type = GitHubUser.class)
	public GitHubUser getUser() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "user") ? null: new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@locked", type = Boolean.class)
	public boolean isLocked() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "locked") ? false: response.get("locked").getAsBoolean();
	}
	
	@GitHubAccessPoint(path = "@repository_url", type = GitHubRepository.class)
	public GitHubRepository getRepository() {
		return this.repo;
	}

	@GitHubAccessPoint(path = "@labels", type = GitHubLabel.class)
	public List<GitHubLabel> getLabels() throws IllegalAccessException, UnsupportedEncodingException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		List<GitHubLabel> labels = new ArrayList<GitHubLabel>();
		
		JsonArray array = response.get("labels").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.get(i).getAsJsonObject();
			labels.add(new GitHubLabel(api, repo, obj.get("name").getAsString(), obj));
		}
		
		return labels;
	}

	@GitHubAccessPoint(path = "@closed_by", type = GitHubUser.class)
	public GitHubUser getClosedBy() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "closed_by") ? null: new GitHubUser(api, response.get("closed_by").getAsJsonObject().get("login").getAsString(), response.get("closed_by").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@milestone", type = GitHubMilestone.class)
	public GitHubMilestone getMilestone() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "milestone") ? null: new GitHubMilestone(api, getRepository(), response.get("milestone").getAsJsonObject().get("number").getAsInt(), response.get("milestone").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@body", type = String.class)
	public String getMessageBody() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "body") ? null: response.get("body").getAsString();
	}

}
