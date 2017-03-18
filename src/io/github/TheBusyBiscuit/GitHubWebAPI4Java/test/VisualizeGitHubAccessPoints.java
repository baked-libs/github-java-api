package io.github.TheBusyBiscuit.GitHubWebAPI4Java.test;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubBranch;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubCommit;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubRepository;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubUser;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class VisualizeGitHubAccessPoints {
	
	private static Map<String, JScrollPane> panes = new HashMap<String, JScrollPane>();
	
	public static void main(String[] args) {
		GitHubWebAPI api = new GitHubWebAPI();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		JFrame frame = new JFrame("GitHub Access Point Visualizer");
		frame.setSize(1420, 940);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabs = new JTabbedPane();
		
		GitHubUser user = api.getUser("TheBusyBiscuit");
		GitHubRepository repo = user.getRepository("Slimefun4");
		GitHubBranch branch = null;
		GitHubCommit commit = null;
		
		try {
			branch = repo.getDefaultBranch();
			commit = branch.getLastCommit();
		} catch (IllegalAccessException e) {
			System.err.println("Connection failed.");
			System.exit(0);
		}
		
		analyseObject(api, gson, user);
		analyseObject(api, gson, repo);
		analyseObject(api, gson, branch);
		analyseObject(api, gson, commit);
		
		System.out.println("Preparing UI...");
		
		Map<String, List<String>> categories = new HashMap<String, List<String>>();
		
		children:
		for (Map.Entry<String, JScrollPane> child: panes.entrySet()) {
			for (Map.Entry<String, JScrollPane> parent: panes.entrySet()) {
				if (child.getKey().equals(parent.getKey())) {
					continue;
				}
				if (child.getKey().split(" | ")[0].startsWith(parent.getKey().split(" | ")[0])) {
					List<String> list = new ArrayList<String>();
					if (categories.containsKey(parent.getKey())) {
						list = categories.get(parent.getKey());
					}
					list.add(child.getKey());
					categories.put(parent.getKey(), list);
					
					continue children;
				}
			}
			
			if (!categories.containsKey(child.getKey())) {
				categories.put(child.getKey(), new ArrayList<String>());
			}
		}
		
		for (Map.Entry<String, List<String>> entry: categories.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				JTabbedPane sub = new JTabbedPane();
				
				sub.add("Main", panes.get(entry.getKey()));
				
				for (String child: entry.getValue()) {
					sub.add(child.split(" | ")[0].replace(entry.getKey().split(" | ")[0], ""), panes.get(child));
				}
				
				tabs.add(entry.getKey().split(" | ")[2], sub);
			}
			else {
				tabs.add(entry.getKey().split(" | ")[2], panes.get(entry.getKey()));
			}
		}
		
		frame.add(tabs);
		
		frame.setVisible(true);
	}
	
	private static void analyseObject(GitHubWebAPI api, Gson gson, GitHubObject object) {
		System.out.println("Scanning '" + object.getURL() + "'...");
		JsonElement element = object.getRawResponseAsJson();
		
		if (element == null) {
			System.err.println("Connection failed.");
			System.out.println(object.getURL());
			System.exit(0);
		}
		
		Map<String, GitHubAccessPoint> queries = getSubURLs(object);
		
		try {
			colorJson(api, true, gson, "", object.getClass(), queries, element);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(element).replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;");
		StringBuilder builder = new StringBuilder();
		
		for (String line: json.split("<br>")) {
			if (line.contains("\\\"GITHUB_ACCESS_POINT\\\"")) {
				builder.append("<font color=#44FF44>" + line.replace("\\\"GITHUB_ACCESS_POINT\\\"", "") + "</font><br>");
			}
			else if (line.endsWith("{") || line.endsWith("}") || line.endsWith("},")) {
				builder.append("<font color=#DDDDDD>" + line + "</font><br>");
			}
			else if (line.endsWith("[") || line.endsWith("]") || line.endsWith("],")) {
				builder.append("<font color=#DDDDDD>" + line + "</font><br>");
			}
			else {
				builder.append("<font color=#FF4444>" + line + "</font><br>");
			}
		}
		
		JEditorPane text = new JEditorPane();
		text.setContentType("text/html");
		text.setBackground(new Color(30, 30, 30));
		text.setText(builder.toString());

		JScrollPane pane = new JScrollPane(text);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		String label = object.getURL() + " | " + object.getClass().getSimpleName().replace("GitHub", "") + (element.isJsonArray() ? "[]": "");
		
		panes.put(label, pane);
	}

	private static void colorJson(GitHubWebAPI api, boolean deep, Gson gson, String path, Class<?> c, Map<String, GitHubAccessPoint> queries, JsonElement element) throws Exception {
		if (element.isJsonArray()) {
			JsonArray array = element.getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				colorJson(api, true, gson, path, c, queries, array.get(i));
			}
		}
		else if (element.isJsonObject()) {
			JsonObject object = element.getAsJsonObject();
			List<Map.Entry<String, JsonElement>> blank = new ArrayList<Map.Entry<String,JsonElement>>();
			List<Map.Entry<String, JsonElement>> entries = new ArrayList<Map.Entry<String,JsonElement>>();
			
			for (Map.Entry<String, JsonElement> json: object.entrySet()) {
				blank.add(json);
				String p = path + (path == "" ? "": "/") + json.getKey();
				
				if (json.getValue() == null || json.getValue().isJsonNull()) {
					
				}
				else if (json.getValue().isJsonObject() || json.getValue().isJsonArray()) {
					for (String url: queries.keySet()) {
						if (url.split(" | ")[0].split("@")[url.split(" | ")[0].split("@").length - 1].equals(json.getKey())) {
							Class<?> cl = queries.get(url).type();
							
							if (GitHubObject.class.isAssignableFrom(cl)) {
								Constructor<?> constructor = getConstructor(cl);
								
								if (constructor != null) {
									GitHubObject content = new GitHubObject(api, null, "");
									Map<String, GitHubAccessPoint> map = getSubURLs((GitHubObject) constructor.newInstance(content));
									colorJson(api, false, gson, p, cl, map, json.getValue());
								}
							}
							else {
								colorJson(api, true, gson, p, c, queries, json.getValue());
							}
							
							break;
						}
					}

					colorJson(api, true, gson, p, c, queries, json.getValue());
				}
				else if (json.getValue().isJsonPrimitive()) {
					if (json.getKey().equals("url")) {
						entries.add(json);
						blank.remove(json);
					}
					else {
						query:
						for (Map.Entry<String, GitHubAccessPoint> entry: queries.entrySet()) {
							if (entry.getKey().split(" | ")[0].contains("@")) {
								String key = entry.getKey().split(" | ")[0].split("@")[entry.getKey().split(" | ")[0].split("@").length - 1];
								if (key.equals(p)) {
									entries.add(json);
									blank.remove(json);
									break query;
								}
							}
							else {
								Pattern pattern = Pattern.compile(entry.getKey().split(" | ")[2]);
								final Matcher matcher = pattern.matcher(json.getValue().getAsString());
								if (matcher.matches()) {
									entries.add(json);
									blank.remove(json);
									
									if (deep && json.getValue().getAsString().contains(entry.getKey().split(" | ")[0]) && GitHubObject.class.isAssignableFrom(entry.getValue().type())) {
										Constructor<?> constructor = getConstructor(entry.getValue().type());
										
										if (constructor != null) {
											GitHubObject content = new GitHubObject(api, null, entry.getKey().split(" | ")[0]);
											
											try {
												analyseObject(api, gson, (GitHubObject) constructor.newInstance(content));
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
									
									break query;
								}
							}
						}
					}
				}
			}
			
			for (Map.Entry<String, JsonElement> entry: entries) {
				object.add("\"GITHUB_ACCESS_POINT\"" + entry.getKey(), entry.getValue());
				object.remove(entry.getKey());
			}
			
			for (Map.Entry<String, JsonElement> entry: blank) {
				object.remove(entry.getKey());
				object.add(entry.getKey(), entry.getValue());
			}
		}
	}

	public static Map<String, GitHubAccessPoint> getSubURLs(GitHubObject object) {
		Map<String, GitHubAccessPoint> queries = new HashMap<String, GitHubAccessPoint>();
		
	    Class<?> c = object.getClass();
	    
	    while (c != GitHubObject.class) {
	        for (final Method method : c.getDeclaredMethods()) {
	            if (method.isAnnotationPresent(GitHubAccessPoint.class)) {
	                Annotation annotation = method.getAnnotation(GitHubAccessPoint.class);
	                queries.put(object.getURL() + ((GitHubAccessPoint) annotation).path() + " | " + object.getRawURL() + ((GitHubAccessPoint) annotation).path() + ".*", (GitHubAccessPoint) annotation);
	            }
	        }
	        
	        c = c.getSuperclass();
	    }
	    
	    return queries;
	}
	
	public static Constructor<?> getConstructor(Class<?> c) {
	    for (Constructor<?> con : c.getConstructors()) {
	    	if (con.getParameterTypes().length == 1 && con.getParameterTypes()[0] == GitHubObject.class) {
	    		return con;
	    	}
	    }
	    return null;
	}

}
