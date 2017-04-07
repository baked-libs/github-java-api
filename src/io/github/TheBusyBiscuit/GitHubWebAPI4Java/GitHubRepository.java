package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubRepository extends UniqueGitHubObject {
	
	public GitHubRepository(GitHubWebAPI api, String username, String repo) {
		super(api, null, "repos/" + username + "/" + repo);
	}
	
	public GitHubRepository(GitHubWebAPI api, String name) {
		super(api, null, "repos/" + name);
	}

	public GitHubRepository(GitHubObject obj) {
		super(obj);
	}
	
	public GitHubRepository(GitHubWebAPI api, String name, JsonElement response) {
		super(api, null, "repos/" + name);
		
		this.minimal = response;
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*";
	}

	@GitHubAccessPoint(path = "/forks", type = GitHubRepository.class)
	public List<GitHubRepository> getForks() throws IllegalAccessException {
		GitHubObject repos = new GitHubObject(api, this, "/forks");
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

	@GitHubAccessPoint(path = "/branches", type = GitHubBranch.class)
	public List<GitHubBranch> getBranches() throws IllegalAccessException {
		GitHubObject branches = new GitHubObject(api, this, "/branches");
		JsonElement response = branches.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubBranch> list = new ArrayList<GitHubBranch>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubBranch branch = new GitHubBranch(api, this, object.get("name").getAsString(), object);
	    	list.add(branch);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/stargazers", type = GitHubUser.class)
	public List<GitHubUser> getStargazers() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/stargazers");
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

	@GitHubAccessPoint(path = "/subscribers", type = GitHubUser.class)
	public List<GitHubUser> getSubscribers() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/subscribers");
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

	@GitHubAccessPoint(path = "/contributors", type = GitHubContributor.class)
	public List<GitHubContributor> getContributors() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/contributors");
		JsonElement response = users.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubContributor> list = new ArrayList<GitHubContributor>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubContributor user = new GitHubContributor(api, object.get("login").getAsString(), object);
	    	list.add(user);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/languages", type = GitHubLanguage.class)
	public List<GitHubLanguage> getLanguages() throws IllegalAccessException {
		GitHubObject langs = new GitHubObject(api, this, "/languages");
		JsonElement response = langs.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubLanguage> list = new ArrayList<GitHubLanguage>();
		JsonObject object = response.getAsJsonObject();
		
		for (Map.Entry<String, JsonElement> entry: object.entrySet()) {
			list.add(new GitHubLanguage(api, this, entry.getKey(), object));
		}
		
		return list;
	}

	@GitHubAccessPoint(path = "/assignees", type = GitHubUser.class)
	public List<GitHubUser> getAvailableAssignees() throws IllegalAccessException {
		GitHubObject users = new GitHubObject(api, this, "/assignees");
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
	    	
	    	GitHubCommit commit = new GitHubCommit(api, this, object.get("sha").getAsString(), object);
	    	list.add(commit);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/downloads", type = GitHubDownload.class)
	public List<GitHubDownload> getDownloads() throws IllegalAccessException {
		GitHubObject downloads = new GitHubObject(api, this, "/downloads");
		JsonElement response = downloads.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubDownload> list = new ArrayList<GitHubDownload>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubDownload download = new GitHubDownload(api, this, object.get("id").getAsInt(), object);
	    	list.add(download);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues() throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", "all");
				
				return params;
			}
			
		};
		
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues(final RepositoryFeature.State state) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", state.toString().toLowerCase());
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues(final GitHubLabel label) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", "all");
				params.put("labels", label.getURLEncodedParameter());
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues(final RepositoryFeature.State state, final GitHubLabel label) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", state.toString().toLowerCase());
				params.put("labels", label.getURLEncodedParameter());
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues(final GitHubMilestone milestone) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", "all");
				try {
					params.put("milestone", String.valueOf(milestone.getNumber()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues(final GitHubLabel label, final GitHubMilestone milestone) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", "all");
				params.put("labels", label.getURLEncodedParameter());
				try {
					params.put("milestone", String.valueOf(milestone.getNumber()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/issues", type = GitHubIssue.class)
	public List<GitHubIssue> getIssues(final RepositoryFeature.State state, final GitHubLabel label, final GitHubMilestone milestone) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/issues") {
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", state.toString().toLowerCase());
				params.put("labels", label.getURLEncodedParameter());
				try {
					params.put("milestone", String.valueOf(milestone.getNumber()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubIssue> list = new ArrayList<GitHubIssue>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubIssue issue = new GitHubIssue(api, this, object.get("number").getAsInt(), object);
	    	list.add(issue);
	    }
		
		return list;
	}
	
	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class)
	public List<GitHubPullRequest> getPullRequests() throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", "all");
				
				return params;
			}
			
		};
		
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<GitHubPullRequest>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class)
	public List<GitHubPullRequest> getPullRequests(final RepositoryFeature.State state) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", state.toString().toLowerCase());
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<GitHubPullRequest>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class)
	public List<GitHubPullRequest> getPullRequests(final GitHubMilestone milestone) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", "all");
				try {
					params.put("milestone", String.valueOf(milestone.getNumber()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<GitHubPullRequest>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/pulls", type = GitHubPullRequest.class)
	public List<GitHubPullRequest> getPullRequests(final RepositoryFeature.State state, final GitHubMilestone milestone) throws IllegalAccessException {
		GitHubObject issues = new GitHubObject(api, this, "/pulls") {
			
			
			@Override
			public Map<String, String> getParameters() {
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("state", state.toString().toLowerCase());
				try {
					params.put("milestone", String.valueOf(milestone.getNumber()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				return params;
			}
			
		};
		JsonElement response = issues.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubPullRequest> list = new ArrayList<GitHubPullRequest>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubPullRequest pr = new GitHubPullRequest(api, this, object.get("number").getAsInt(), object);
	    	list.add(pr);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/labels", type = GitHubLabel.class)
	public List<GitHubLabel> getLabels() throws IllegalAccessException, UnsupportedEncodingException {
		GitHubObject labels = new GitHubObject(api, this, "/labels");
		JsonElement response = labels.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubLabel> list = new ArrayList<GitHubLabel>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubLabel issue = new GitHubLabel(api, this, object.get("name").getAsString(), object);
	    	list.add(issue);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "/milestones", type = GitHubMilestone.class)
	public List<GitHubMilestone> getMilestones() throws IllegalAccessException, UnsupportedEncodingException {
		GitHubObject labels = new GitHubObject(api, this, "/milestones");
		JsonElement response = labels.getResponse(true);
		
		if (response == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		List<GitHubMilestone> list = new ArrayList<GitHubMilestone>();
		JsonArray array = response.getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();
	    	
	    	GitHubMilestone milestone = new GitHubMilestone(api, this, object.get("number").getAsInt(), object);
	    	list.add(milestone);
	    }
		
		return list;
	}

	@GitHubAccessPoint(path = "@owner", type = GitHubUser.class)
	public GitHubUser getOwner() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "owner") ? null: new GitHubUser(api, response.get("owner").getAsJsonObject().get("login").getAsString(), response.get("owner").getAsJsonObject());
	}

	@GitHubAccessPoint(path = "@name", type = String.class)
	public String getName() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "name") ? null: response.get("name").getAsString();
	}

	@GitHubAccessPoint(path = "@full_name", type = String.class)
	public String getFullName() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();
		
		return isInvalid(response, "full_name") ? null: response.get("full_name").getAsString();
	}

	@GitHubAccessPoint(path = "@description", type = String.class)
	public String getDescription() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "description") ? null: response.get("description").getAsString();
	}

	@GitHubAccessPoint(path = "@fork", type = Boolean.class)
	public boolean isFork() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "fork") ? false: response.get("fork").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@has_issues", type = Boolean.class)
	public boolean hasIssues() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "has_issues") ? false: response.get("has_issues").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@has_downloads", type = Boolean.class)
	public boolean hasDownloads() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "has_downloads") ? false: response.get("has_downloads").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@has_wiki", type = Boolean.class)
	public boolean hasWiki() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "has_wiki") ? false: response.get("has_wiki").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@has_pages", type = Boolean.class)
	public boolean hasPages() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "has_pages") ? false: response.get("has_pages").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@has_projects", type = Boolean.class)
	public boolean hasProjects() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "has_projects") ? false: response.get("has_projects").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@homepage", type = String.class)
	public String getWebsite() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "homepage") ? null: response.get("homepage").getAsString();
	}

	@GitHubAccessPoint(path = "@size", type = Integer.class)
	public int getSize() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "size") ? null: response.get("size").getAsInt();
	}

	@GitHubAccessPoint(path = "@stargazers_count", type = Integer.class)
	public int getStargazersAmount() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "stargazers_count") ? null: response.get("stargazers_count").getAsInt();
	}

	@GitHubAccessPoint(path = "@watchers_count", type = Integer.class)
	public int getWatchersAmount() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "watchers_count") ? null: response.get("watchers_count").getAsInt();
	}

	@GitHubAccessPoint(path = "@forks_count", type = Integer.class)
	public int getForksAmount() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "forks_count") ? null: response.get("forks_count").getAsInt();
	}

	@GitHubAccessPoint(path = "@open_issues_count", type = Integer.class)
	public int getOpenIssuesAmount() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "open_issues_count") ? null: response.get("open_issues_count").getAsInt();
	}

	@GitHubAccessPoint(path = "@subscribers_count", type = Integer.class)
	public int getSubscribersAmount() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "subscribers_count") ? null: response.get("subscribers_count").getAsInt();
	}

	@GitHubAccessPoint(path = "@language", type = GitHubLanguage.class)
	public GitHubLanguage getDominantLanguage() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "language") ? null: new GitHubLanguage(api, this, response.get("language").getAsString());
	}

	@GitHubAccessPoint(path = "@default_branch", type = GitHubBranch.class)
	public GitHubBranch getDefaultBranch() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "default_branch") ? null: new GitHubBranch(api, this, response.get("default_branch").getAsString());
	}

	public GitHubBranch getBranch(String name) throws IllegalAccessException {
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

	@GitHubAccessPoint(path = "@pushed_at", type = Date.class)
	public Date getLastPushedDate() throws IllegalAccessException {
		JsonElement element = getResponse(false);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		JsonObject response = element.getAsJsonObject();

		return isInvalid(response, "pushed_at") ? null: GitHubDate.parse(response.get("pushed_at").getAsString());
	}
	
	@GitHubAccessPoint(path = "/git/refs", type = GitHubReference.class)
	public List<GitHubReference> getReferences() throws IllegalAccessException {
		GitHubObject repos = new GitHubObject(api, this, "/git/refs");
		JsonElement response = repos.getResponse(true);
		
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
}
