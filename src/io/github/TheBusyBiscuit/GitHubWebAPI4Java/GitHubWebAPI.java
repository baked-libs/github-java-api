package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra.Base64url;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra.CacheMode;

public class GitHubWebAPI {
	
	private String token = "";
	protected String hard_drive_cache = null;
	protected CacheMode cache_mode;
	public Map<String, JsonElement> cache = new HashMap<>();
	
	public static int ITEMS_PER_PAGE = 100;
	
	public GitHubWebAPI() {
		this.cache_mode = CacheMode.RAM_CACHE;
	}
	
	public GitHubWebAPI(CacheMode mode) {
		this.cache_mode = mode;
	}
	
	public GitHubWebAPI(String access_token) {
		this.token = access_token;
		this.cache_mode = CacheMode.RAM_CACHE;
	}
	
	public String getAccessToken() {
		return this.token;
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
			String token = getAccessToken();
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
	        	json.addProperty("code", connection.getResponseCode());
	        	
	        	return json;
	        }
		} catch (SocketTimeoutException e) {
        	JsonObject json = new JsonObject();
        	json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
        	json.addProperty("documentation_url", "404");
        	json.addProperty("exception", e.getClass().getSimpleName());
        	
        	return json;
		} catch (MalformedURLException e) {
        	JsonObject json = new JsonObject();
        	json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
        	json.addProperty("documentation_url", "404");
        	json.addProperty("exception", e.getClass().getSimpleName());
        	
        	return json;
		} catch (IOException e) {
        	JsonObject json = new JsonObject();
        	json.addProperty("message", e.getClass().getName() + " - " + e.getLocalizedMessage());
        	json.addProperty("documentation_url", "404");
        	json.addProperty("exception", e.getClass().getSimpleName());
        	
        	return json;
		}
	}
	
	public void clearCache() {
		this.cache.clear();
	}

	public void cache(String url, JsonElement response) {
		switch(this.cache_mode) {
			case HARD_DRIVE_CACHE: {
				if (hard_drive_cache != null) {
					try {
						saveHardDriveCache(Base64url.encode(url) + ".json", response);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case RAM_AND_HARD_DRIVE_CACHE: {
				cache.put(url, response);
				
				if (hard_drive_cache != null) {
					try {
						saveHardDriveCache(Base64url.encode(url) + ".json", response);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case RAM_CACHE: {
				cache.put(url, response);
				break;
			}
			default: {
				break;
			}
		}
	}
	
	protected JsonElement readHardDriveCache(String file) throws IOException {
		if (new File(hard_drive_cache + file).exists()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(hard_drive_cache + file)), StandardCharsets.UTF_8));
			
			String data = reader.readLine();
			
		    reader.close();
			return new JsonParser().parse(data);
		}
		
		return null;
	}
	
	protected void saveHardDriveCache(String file, JsonElement json) throws FileNotFoundException {
		Gson gson = new GsonBuilder().serializeNulls().create();
	    PrintWriter writer = new PrintWriter(hard_drive_cache + file);
	    writer.println(gson.toJson(json));
	    writer.close();
	}

	public void setupHardDriveCache(String path) throws IOException {
		File dir = new File(path);
		if (!dir.exists()) dir.mkdirs();
		
		this.hard_drive_cache = path + "/";
	}
	
	public void setCacheMode(CacheMode mode) {
		this.cache_mode = mode;
	}
	
	public CacheMode getCacheMode() {
		return this.cache_mode;
	}
	
	public void disableCaching() {
		this.cache_mode = CacheMode.NO_CACHE;
	}
}
