package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class GitHubObject extends Object {
	
	protected GitHubWebAPI api;
	protected GitHubObject parent;
	protected String suffix;
	
	protected JsonElement response = null;
	protected JsonElement minimal = null;

	public GitHubObject(GitHubWebAPI api, GitHubObject parent, String suffix) {
		this.api = api;
		this.parent = parent;
		this.suffix = suffix;
	}

	public GitHubObject(GitHubObject object) {
		this.api = object.api;
		this.parent = object.parent;
		this.suffix = object.suffix;
	}

	public String getURL() {
		return this.getURL(suffix);
	}
	
	protected String getFullURL() {
		String query = getURL();
		
		if (getParameters() != null) {
			boolean first = true;
			
			for (Map.Entry<String, String> parameter: getParameters().entrySet()) {
				query += (first ? "?": "&") + parameter.getKey() + "=" + parameter.getValue();
				
				first = false;
			}
		}
		
		return query;
	}

	@GitHubAccessPoint(path = "@url", type = String.class, requiresAccessToken = false)
	protected String getURL(String suffix) {
		if (parent == null) {
			return suffix;
		}
		
		StringBuilder builder = new StringBuilder();

		builder.append(new StringBuilder(suffix).reverse().toString());
		
		GitHubObject o = getParent();
		while (o != null) {
			builder.append(new StringBuilder(o.getSuffix()).reverse().toString());
			
			o = o.getParent();
		}
		
		return builder.reverse().toString();
	}

	public String getRawURL() {
		return this.getURL();
	}
	
	public JsonElement getRawResponseAsJson() {
		return this.getResponse(true);
	}
	
	protected JsonElement getResponse(boolean full) {
		if (!full && minimal != null) {
			return minimal;
		}
		
		if (response != null) {
			if (response.isJsonObject() && ((JsonObject) response).has("documentation_url")) {
				return null;
			}
			
			return response;
		}
		
		if (api.cache.containsKey(getFullURL())) {
			response = api.cache.get(getFullURL());
		}
		else {
			this.response = api.call(this);
			
			if (response.isJsonObject() && ((JsonObject) response).has("documentation_url")) {
				return null;
			}
			
			api.cache.put(getFullURL(), response);
		}
		
		return response;
	}
	
	protected boolean isInvalid(JsonObject response, String key) {
		if (!response.has(key)) {
			return true;
		}
		if (response.get(key).isJsonNull()) {
			return true;
		}
		if (response.get(key) == null) {
			return true;
		}
		
		return false;
	}

	protected String getSuffix() {
		return this.suffix;
	}

	protected GitHubObject getParent() {
		return this.parent;
	}
	
	public void clearCache() {
		String url = getURL();
		if (api.cache.containsKey(url)) {
			api.cache.remove(url);
		}
		
		if (response != null) {
			response = null;
		}
	}

	public Map<String, String> getParameters() {
		return null;
	}
	
}
