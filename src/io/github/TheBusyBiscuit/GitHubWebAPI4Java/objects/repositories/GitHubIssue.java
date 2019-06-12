package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.RepositoryFeature;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

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

	@GitHubAccessPoint(path = "@user", type = GitHubUser.class, requiresAccessToken = false)
	public GitHubUser getUser() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "user") ? null: new GitHubUser(api, response.get("user").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@locked", type = Boolean.class, requiresAccessToken = false)
	public boolean isLocked() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "locked") ? false: response.get("locked").getAsBoolean();
	}
	
	@GitHubAccessPoint(path = "@repository_url", type = GitHubRepository.class, requiresAccessToken = false)
	public GitHubRepository getRepository() {
		return this.repo;
	}

	@GitHubAccessPoint(path = "@labels", type = GitHubLabel.class, requiresAccessToken = false)
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

	@GitHubAccessPoint(path = "@closed_by", type = GitHubUser.class, requiresAccessToken = false)
	public GitHubUser getClosedBy() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "closed_by") ? null: new GitHubUser(api, response.get("closed_by").getAsJsonObject().get("login").getAsString(), response.get("closed_by").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@milestone", type = GitHubMilestone.class, requiresAccessToken = false)
	public GitHubMilestone getMilestone() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "milestone") ? null: new GitHubMilestone(api, getRepository(), response.get("milestone").getAsJsonObject().get("number").getAsInt(), response.get("milestone").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@body", type = String.class, requiresAccessToken = false)
	public String getMessageBody() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "body") ? null: response.get("body").getAsString();
	}

	@GitHubAccessPoint(path = "@assignees", type = GitHubUser.class, requiresAccessToken = false)
	public List<GitHubUser> getAssignees() throws IllegalAccessException, UnsupportedEncodingException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		List<GitHubUser> users = new ArrayList<GitHubUser>();
		
		JsonArray array = response.get("assignees").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.get(i).getAsJsonObject();
			users.add(new GitHubUser(api, obj.get("login").getAsString(), obj));
		}
		
		return users;
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

	@GitHubAccessPoint(path = "@comments", type = Integer.class, requiresAccessToken = false)
	public int getCommentsAmount() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "comments") ? null: response.get("comments").getAsInt();
	}

}
