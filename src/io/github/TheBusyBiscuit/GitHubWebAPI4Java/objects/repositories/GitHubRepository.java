package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.RepositoryFeature;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.UniqueGitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

/**
 * Represents a GitHub repository.
 */
public class GitHubRepository extends UniqueGitHubObject {
	
	private String fullName = null;
	
	public GitHubRepository(GitHubWebAPI api, String username, String repo) {
		super(api, null, "repos/" + username + "/" + repo);
		
		this.fullName = username + "/" + repo;
	}
	
	public GitHubRepository(GitHubWebAPI api, String name) {
		super(api, null, "repos/" + name);
		
		this.fullName = name;
	}

	public GitHubRepository(GitHubObject obj) {
		super(obj);
	}
	
	public GitHubRepository(GitHubWebAPI api, String name, JsonElement response) {
		super(api, null, "repos/" + name);
		
		this.fullName = name;
		this.minimal = response;
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*";
	}

	@GitHubAccessPoint(path = "/forks", type = GitHubRepository.class, requiresAccessToken = false)
	public List<GitHubRepository> getForks() throws IllegalAccessException {
		return getForks(1);
	}

	@GitHubAccessPoint(path = "/forks", type = GitHubRepository.class, requiresAccessToken = false)
	public List<GitHubRepository> getAllForks() throws IllegalAccessException {
		List<GitHubRepository> forks = new ArrayList<>();
		
		int i = 2;
		List<GitHubRepository> temp = getForks(1);
		
		while (!temp.isEmpty()) {
			forks.addAll(temp);
			
			temp = getForks(i);
			i++;
		}
		
		return forks;
	}

	@GitHubAccessPoint(path = "/forks", type = GitHubRepository.class, requiresAccessToken = false)
	public List<GitHubRepository> getForks(final int page) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(GitHubWebAPI.ITEMS_PER_PAGE));
		
		GitHubObject forks = new GitHubObject(api, this, "/forks") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		
		JsonElement response = forks.getResponse(true);
		
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
	
	@GitHubAccessPoint(path = "/branches", type = GitHubBranch.class, requiresAccessToken = false)
	public List<GitHubBranch> getBranches() throws IllegalAccessException, UnsupportedEncodingException {
		return getBranches(1);
	}

	@GitHubAccessPoint(path = "/branches", type = GitHubBranch.class, requiresAccessToken = false)
	public List<GitHubBranch> getAllBranches() throws IllegalAccessException, UnsupportedEncodingException {
		List<GitHubBranch> branches = new ArrayList<>();
		
		int i = 2;
		List<GitHubBranch> temp = getBranches(1);
		
		while (!temp.isEmpty()) {
			branches.addAll(temp);
			
			temp = getBranches(i);
			i++;
		}
		
		return branches;
	}

	@GitHubAccessPoint(path = "/branches", type = GitHubBranch.class, requiresAccessToken = false)
	public List<GitHubBranch> getBranches(final int page) throws IllegalAccessException, UnsupportedEncodingException {
		final Map<String, String> params = new HashMap<>();
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(GitHubWebAPI.ITEMS_PER_PAGE));
		GitHubObject branches = new GitHubObject(api, this, "/branches") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = branches.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + branches.getURL() + "'");
		}
		
		List<GitHubBranch> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubBranch branch = new GitHubBranch(api, this, object.get("name").getAsString(), object);
	    	list.add(branch);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/stargazers", type = GitHubUser.class, requiresAccessToken = false)
	public List<GitHubUser> getStargazers() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/stargazers");
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

	@GitHubAccessPoint(path = "/subscribers", type = GitHubUser.class, requiresAccessToken = false)
	public List<GitHubUser> getSubscribers() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/subscribers");
		JsonElement response = users.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + users.getURL() + "'");
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

	@GitHubAccessPoint(path = "/contributors", type = GitHubContributor.class, requiresAccessToken = false)
	public List<GitHubContributor> getContributors() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/contributors");
		JsonElement response = users.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + users.getURL() + "'");
		}
		
		List<GitHubContributor> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubContributor user = new GitHubContributor(api, object.get("login").getAsString(), object);
	    	list.add(user);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/collaborators", type = GitHubCollaborator.class, requiresAccessToken = true)
	public List<GitHubCollaborator> getCollaborators() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/collaborators");
		JsonElement response = users.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + users.getURL() + "'");
		}
		
		List<GitHubCollaborator> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubCollaborator user = new GitHubCollaborator(api, object.get("login").getAsString(), object);
	    	list.add(user);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/languages", type = GitHubLanguage.class, requiresAccessToken = false)
	public List<GitHubLanguage> getLanguages() throws IllegalAccessException {
		GitHubObject langs = new GitHubObject(api, this, "/languages");
		JsonElement response = langs.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + langs.getURL() + "'");
		}
		
		List<GitHubLanguage> list = new ArrayList<>();
		JsonObject object = response.getAsJsonObject();
		
		for (Map.Entry<String, JsonElement> entry: object.entrySet()) {
			list.add(new GitHubLanguage(api, this, entry.getKey(), object));
		}
		
		return list;
	}

	@GitHubAccessPoint(path = "/assignees", type = GitHubUser.class, requiresAccessToken = false)
	public List<GitHubUser> getAvailableAssignees() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/assignees");
		JsonElement response = users.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + users.getURL() + "'");
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

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
	public List<GitHubCommit> getCommits() throws IllegalAccessException {
		return this.getCommits(1);
	}

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
	public List<GitHubCommit> getAllCommits() throws IllegalAccessException {
		List<GitHubCommit> commits = new ArrayList<>();
		
		int i = 2;
		List<GitHubCommit> temp = getCommits(1);
		
		while (!temp.isEmpty()) {
			commits.addAll(temp);
			
			temp = getCommits(i);
			i++;
		}
		
		return commits;
	}

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
	public List<GitHubCommit> getAllCommits(GitHubUser author) throws IllegalAccessException {
		List<GitHubCommit> commits = new ArrayList<>();
		
		int i = 2;
		List<GitHubCommit> temp = getCommits(1, author);
		
		while (!temp.isEmpty()) {
			commits.addAll(temp);
			
			temp = getCommits(i, author);
			i++;
		}
		
		return commits;
	}

	public GitHubCommit getCommit(String sha) {
		return new GitHubCommit(api, this, sha);
	}

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
	public List<GitHubCommit> getCommits(final int page) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(GitHubWebAPI.ITEMS_PER_PAGE));
		
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
	    	
	    	GitHubCommit commit = new GitHubCommit(api, this, object.get("sha").getAsString(), object);
	    	list.add(commit);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/commits", type = GitHubCommit.class, requiresAccessToken = false)
	public List<GitHubCommit> getCommits(final int page, final GitHubUser author) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(GitHubWebAPI.ITEMS_PER_PAGE));
		params.put("author", author.getUsername());
		
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
		
		List<GitHubCommit> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubCommit commit = new GitHubCommit(api, this, object.get("sha").getAsString(), object);
	    	list.add(commit);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/downloads", type = GitHubDownload.class, requiresAccessToken = false)
	public List<GitHubDownload> getDownloads() throws IllegalAccessException {
		GitHubObject downloads = new GitHubObject(api, this, "/downloads");
		JsonElement response = downloads.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubDownload> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubDownload download = new GitHubDownload(api, this, object.get("id").getAsInt(), object);
	    	list.add(download);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues() throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", "all");
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues(final RepositoryFeature.State state) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", state.toString().toLowerCase());
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	/**
	 * Returns a list of all the issues of this repository that are labelled with the corresponding {@link GitHubLabel}.
	 * @param label the {@link GitHubLabel} to inquire, not null.
	 * @return a list of all the issues that are labelled with the corresponding {@link GitHubLabel}, empty if none.
	 * @throws IllegalAccessException if a connection could not be established.
	 */
	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues(final GitHubLabel label) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", "all");
		params.put("labels", label.getURLEncodedParameter());
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues(final RepositoryFeature.State state, final GitHubLabel label) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", state.toString().toLowerCase());
		params.put("labels", label.getURLEncodedParameter());
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues(final GitHubMilestone milestone) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", "all");
		try {
			params.put("milestone", String.valueOf(milestone.getNumber()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues(final GitHubLabel label, final GitHubMilestone milestone) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", "all");
		params.put("labels", label.getURLEncodedParameter());
		try {
			params.put("milestone", String.valueOf(milestone.getNumber()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class, requiresAccessToken = false)
	public List<GitHubIssue> getIssues(final RepositoryFeature.State state, final GitHubLabel label, final GitHubMilestone milestone) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", state.toString().toLowerCase());
		params.put("labels", label.getURLEncodedParameter());
		try {
			params.put("milestone", String.valueOf(milestone.getNumber()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}
	
	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class, requiresAccessToken = false)
	public List<GitHubPullRequest> getPullRequests() throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", "all");
		
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class, requiresAccessToken = false)
	public List<GitHubPullRequest> getPullRequests(final RepositoryFeature.State state) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", state.toString().toLowerCase());
		
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class, requiresAccessToken = false)
	public List<GitHubPullRequest> getPullRequests(final GitHubMilestone milestone) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", "all");
		try {
			params.put("milestone", String.valueOf(milestone.getNumber()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class, requiresAccessToken = false)
	public List<GitHubPullRequest> getPullRequests(final RepositoryFeature.State state, final GitHubMilestone milestone) throws IllegalAccessException {
		final Map<String, String> params = new HashMap<>();
		params.put("state", state.toString().toLowerCase());
		try {
			params.put("milestone", String.valueOf(milestone.getNumber()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			@Override
			public Map<String, String> getParameters() {
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/labels", type = GitHubLabel.class, requiresAccessToken = false)
	public List<GitHubLabel> getLabels() throws IllegalAccessException, UnsupportedEncodingException {
		GitHubObject labels = new GitHubObject(api, this, "/labels");
		JsonElement response = labels.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubLabel> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubLabel issue = new GitHubLabel(api, this, object.get("name").getAsString(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/milestones", type = GitHubMilestone.class, requiresAccessToken = false)
	public List<GitHubMilestone> getMilestones() throws IllegalAccessException, UnsupportedEncodingException {
		GitHubObject labels = new GitHubObject(api, this, "/milestones");
		JsonElement response = labels.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubMilestone> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubMilestone milestone = new GitHubMilestone(api, this, object.get("number").getAsInt(), object);
	    	list.add(milestone);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "@owner", type = GitHubUser.class, requiresAccessToken = false)
	public GitHubUser getOwner() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "owner") ? null: new GitHubUser(api, response.get("owner").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@name", type = String.class, requiresAccessToken = false)
	public String getName() throws IllegalAccessException {
		return getString("name", false);
	}

	@GitHubAccessPoint(path = "@full_name", type = String.class, requiresAccessToken = false)
	public String getFullName() throws IllegalAccessException {
		return getString("full_name", false);
	}

	@GitHubAccessPoint(path = "@description", type = String.class, requiresAccessToken = false)
	public String getDescription() throws IllegalAccessException {
		return getString("description", false);
	}

	@GitHubAccessPoint(path = "@fork", type = Boolean.class, requiresAccessToken = false)
	public Boolean isFork() throws IllegalAccessException {
		return getBoolean("fork", false);
	}

	@GitHubAccessPoint(path = "@has_issues", type = Boolean.class, requiresAccessToken = false)
	public Boolean hasIssues() throws IllegalAccessException {
		return getBoolean("has_issues", false);
	}

	@GitHubAccessPoint(path = "@has_downloads", type = Boolean.class, requiresAccessToken = false)
	public Boolean hasDownloads() throws IllegalAccessException {
		return getBoolean("has_downloads", false);
	}

	@GitHubAccessPoint(path = "@has_wiki", type = Boolean.class, requiresAccessToken = false)
	public Boolean hasWiki() throws IllegalAccessException {
		return getBoolean("has_wiki", false);
	}

	@GitHubAccessPoint(path = "@has_pages", type = Boolean.class, requiresAccessToken = false)
	public Boolean hasPages() throws IllegalAccessException {
		return getBoolean("has_pages", false);
	}

	@GitHubAccessPoint(path = "@has_projects", type = Boolean.class, requiresAccessToken = false)
	public Boolean hasProjects() throws IllegalAccessException {
		return getBoolean("has_pages", false);
	}

	@GitHubAccessPoint(path = "@homepage", type = String.class, requiresAccessToken = false)
	public String getWebsite() throws IllegalAccessException {
		return getString("homepage", false);
	}

	@GitHubAccessPoint(path = "@size", type = Integer.class, requiresAccessToken = false)
	public Integer getSize() throws IllegalAccessException {
		return getInteger("size", false);
	}

	@GitHubAccessPoint(path = "@stargazers_count", type = Integer.class, requiresAccessToken = false)
	public Integer getStargazersAmount() throws IllegalAccessException {
		return getInteger("stargazers_count", false);
	}

	@GitHubAccessPoint(path = "@watchers_count", type = Integer.class, requiresAccessToken = false)
	public Integer getWatchersAmount() throws IllegalAccessException {
		return getInteger("watchers_count", false);
	}

	@GitHubAccessPoint(path = "@forks_count", type = Integer.class, requiresAccessToken = false)
	public Integer getForksAmount() throws IllegalAccessException {
		return getInteger("forks_count", false);
	}

	@GitHubAccessPoint(path = "@open_issues_count", type = Integer.class, requiresAccessToken = false)
	public Integer getOpenIssuesAmount() throws IllegalAccessException {
		return getInteger("open_issues_count", false);
	}

	@GitHubAccessPoint(path = "@subscribers_count", type = Integer.class, requiresAccessToken = false)
	public Integer getSubscribersAmount() throws IllegalAccessException {
		return getInteger("subscribers_count", true);
	}

	@GitHubAccessPoint(path = "@language", type = GitHubLanguage.class, requiresAccessToken = false)
	public GitHubLanguage getDominantLanguage() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "language") ? null: new GitHubLanguage(api, this, response.get("language").getAsString());
	}

	@GitHubAccessPoint(path = "@default_branch", type = GitHubBranch.class, requiresAccessToken = false)
	public GitHubBranch getDefaultBranch() throws IllegalAccessException, UnsupportedEncodingException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "default_branch") ? null: new GitHubBranch(api, this, response.get("default_branch").getAsString());
	}

	public GitHubBranch getBranch(String name) throws IllegalAccessException, UnsupportedEncodingException {
		return new GitHubBranch(api, this, name);
	}

	public GitHubIssue getIssue(int number) throws IllegalAccessException {
		return new GitHubIssue(api, this, number);
	}

	public GitHubPullRequest getPullRequest(int number) throws IllegalAccessException {
		return new GitHubPullRequest(api, this, number);
	}

	public GitHubMilestone getMilestone(int number) throws IllegalAccessException {
		return new GitHubMilestone(api, this, number);
	}

	public GitHubLabel getLabel(String name) throws IllegalAccessException, UnsupportedEncodingException {
		return new GitHubLabel(api, this, name);
	}

	@GitHubAccessPoint(path = "@pushed_at", type = Date.class, requiresAccessToken = false)
	public Date getLastPushedDate() throws IllegalAccessException {
		return getDate("pushed_at", false);
	}
	
	@GitHubAccessPoint(path = "/git/refs", type = GitHubReference.class, requiresAccessToken = false)
	public List<GitHubReference> getReferences() throws IllegalAccessException {
		GitHubObject refs = new GitHubObject(api, this, "/git/refs");
		JsonElement response = refs.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubReference> list = new ArrayList<GitHubReference>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubReference ref = new GitHubReference(api, this, object.get("ref").getAsString(), object);
	    	list.add(ref);
	    }
		
		return list;
	}
	
	@GitHubAccessPoint(path = "/tags", type = GitHubTag.class, requiresAccessToken = false)
	public List<GitHubTag> getTags() throws IllegalAccessException {
		GitHubObject tags = new GitHubObject(api, this, "/tags");
		JsonElement response = tags.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubTag> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubTag tag = new GitHubTag(api, this, object);
	    	list.add(tag);
	    }
		
		return list;
	}
	
	@GitHubAccessPoint(path = "/comments", type = GitHubComment.class, requiresAccessToken = false)
	public List<GitHubComment> getCommitComments() throws IllegalAccessException {
		GitHubObject comments = new GitHubObject(api, this, "/comments");
		JsonElement response = comments.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubComment> list = new ArrayList<>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubComment comment = new GitHubComment(api, this, object.get("id").getAsInt(), object);
	    	list.add(comment);
	    }
		
		return list;
	}
	
	@GitHubAccessPoint(path = "@parent", type = GitHubRepository.class, requiresAccessToken = false)
	public GitHubRepository getForkParent() throws IllegalAccessException {
		if (!isFork()) return null;
		
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "parent") ? null: new GitHubRepository(api, response.get("parent").getAsJsonObject().get("full_name").getAsString(), response.get("parent").getAsJsonObject());
	}
	
	@GitHubAccessPoint(path = "@source", type = GitHubRepository.class, requiresAccessToken = false)
	public GitHubRepository getForkSource() throws IllegalAccessException {
		if (!isFork()) return null;
		
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "source") ? null: new GitHubRepository(api, response.get("source").getAsJsonObject().get("full_name").getAsString(), response.get("source").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@private", type = Boolean.class, requiresAccessToken = false)
	public Boolean isPrivate() throws IllegalAccessException {
		return getBoolean("private", false);
	}

	@GitHubAccessPoint(path = "@archived", type = Boolean.class, requiresAccessToken = false)
	public Boolean isArchived() throws IllegalAccessException {
		return getBoolean("archived", false);
	}
	
	@GitHubAccessPoint(path = "@license", type = GitHubLicense.class, requiresAccessToken = false)
	public GitHubLicense getLicense() throws IllegalAccessException, UnsupportedEncodingException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "license") ? null: new GitHubLicense(api, response.get("license").getAsJsonObject().get("key").getAsString(), response.get("license").getAsJsonObject());
	}
}
