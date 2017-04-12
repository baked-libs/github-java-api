package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GitHubWebAPI {
	
	private String token = "";
	public Map<String, JsonElement> cache = new HashMap<String, JsonElement>();
	
	public static int ITEMS_PER_PAGE = 100;
	
	public GitHubWebAPI() {
		
	}
	
	public GitHubWebAPI(String access_token) {
		this.token = access_token;
	}
	
	public String getURL() {
		return "https://api.github.com/";
	}
	
	public GitHubUser getUser(String username) {
		return new GitHubUser(this, username);
	}
	
	public GitHubOrganization getOrganization(String username) {
		return new GitHubOrganization(this, username);
	}
	
	public GitHubRepository getRepository(String username, String repo) {
		return new GitHubRepository(this, username + "/" + repo);
	}
	
	public JsonElement call(GitHubObject object) {
		try {
			String query = getURL() + object.getURL();
			
			if (token != "") {
				query += "?access_token=" + token;
				
				if (object.getParameters() != null) {
					for (Map.Entry<String, String> parameter: object.getParameters().entrySet()) {
						query += "&" + parameter.getKey() + "=" + parameter.getValue();
					}
				}
			}
			else {
				if (object.getParameters() != null) {
					boolean first = true;
					
					for (Map.Entry<String, String> parameter: object.getParameters().entrySet()) {
						query += (first ? "?": "&") + parameter.getKey() + "=" + parameter.getValue();
						
						first = false;
					}
				}
			}
			
			URL website = new URL(query);
			
			HttpURLConnection connection = (HttpURLConnection) website.openConnection();
	        connection.setConnectTimeout(5000);
	        connection.addRequestProperty("User-Agent", "GitHub Web API 4 Java (by TheBusyBiscuit)");
	        connection.setDoOutput(true);
			
	        if (connection.getResponseCode() == 200) {
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        	
	        	StringBuilder buffer = new StringBuilder();
	        	
	        	String line;
	        	
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				
				reader.close();
	     	    connection.disconnect();
	     	    
	     	    JsonElement json = new JsonParser().parse(buffer.toString());
	     	    
	     	    return json;
	        }
	        else {
	     	    connection.disconnect();
	     	    
	        	JsonObject json = new JsonObject();
	        	json.addProperty("message", connection.getResponseCode() + " - " + connection.getResponseMessage());
	        	json.addProperty("documentation_url", "https://developer.github.com/v3");
	        	
	        	return json;
	        }
		} catch (MalformedURLException e) {
        	JsonObject json = new JsonObject();
        	json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
        	json.addProperty("documentation_url", "404");
        	
        	return json;
		} catch (IOException e) {
        	JsonObject json = new JsonObject();
        	json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
        	json.addProperty("documentation_url", "404");
        	
        	return json;
		}
	}
	
	public void clearCache() {
		this.cache.clear();
	}

}
