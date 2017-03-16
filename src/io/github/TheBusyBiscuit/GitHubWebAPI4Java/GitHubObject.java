package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GitHubObject {
	
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
		
		if (api.cache.containsKey(getURL())) {
			response = api.cache.get(getURL());
		}
		else {
			this.response = api.call(this);
			
			if (response.isJsonObject() && ((JsonObject) response).has("documentation_url")) {
				return null;
			}
			
			api.cache.put(getURL(), response);
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
