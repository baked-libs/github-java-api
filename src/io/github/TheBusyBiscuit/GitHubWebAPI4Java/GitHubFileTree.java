package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubFileTree extends GitHubFile {
	
	private Map<String, String> params = new HashMap<String, String>();
	
	public GitHubFileTree(GitHubWebAPI api, GitHubRepository repo, String id, boolean recursive) {
		super(api, repo, "/git/trees/" + id);
		
		if (recursive) {
			params.put("recursive", "1");
		}
	}
	
	public GitHubFileTree(GitHubWebAPI api, GitHubRepository repo, String id, JsonElement response, boolean recursive) {
		super(api, repo, "/git/trees/" + id, response);
		
		if (recursive) {
			params.put("recursive", "1");
		}
	}

	public GitHubFileTree(GitHubObject obj) {
		super(obj);
	}
	
	@Override
	public String getRawURL() {
		return ".*repos/.*/.*/git/trees/.*";
	}
	
	@Override
	public Map<String, String> getParameters() {
		return params;
	}

	@GitHubAccessPoint(path = "@tree", type = GitHubFile.class, requiresAccessToken = false)
	public List<GitHubFile> getFiles() throws IllegalAccessException {
		JsonElement element = getResponse(true);
		
		if (element == null) {
			throw new IllegalAccessException("Could not connect to '" + getURL() + "'");
		}
		
		JsonObject response = element.getAsJsonObject();
		
		List<GitHubFile> files = new ArrayList<GitHubFile>();
		
		JsonArray array = response.get("tree").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.get(i).getAsJsonObject();
			String type = obj.get("type").getAsString();
			if (type.equals("blob")) {
				files.add(new GitHubBlob(api, getRepository(), obj.get("sha").getAsString(), obj));
			}
			else if (type.equals("tree")) {
				files.add(new GitHubFileTree(api, getRepository(), obj.get("sha").getAsString(), obj, false));
			}
		}
		
		return files;
	}
}
