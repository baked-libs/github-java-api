package io.github.TheBusyBiscuit.GitHubWebAPI4Java.test;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubFileTree;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubRepository;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubUser;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class AccessPointVisualizer {
	
	public static Map<String, JCallbackDisplay> panes = new HashMap<String, JCallbackDisplay>();
	public static Map<String, List<String>> categories = new HashMap<String, List<String>>();
	
	public static void main(String[] args) {
		run(true);
	}
	
	protected static void run(boolean openVisualizer) {
		GitHubWebAPI api = new GitHubWebAPI();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		GitHubUser user = api.getUser("TheBusyBiscuit");
		GitHubRepository repo = user.getRepository("Slimefun4");
		GitHubBranch branch = null;
		GitHubCommit commit = null;
		GitHubFileTree tree = null;
		
		try {
			branch = repo.getDefaultBranch();
			commit = branch.getLastCommit();
			tree = commit.getFileTree();
		} catch (IllegalAccessException e) {
			System.err.println("Connection failed.");
			System.exit(0);
		}
		
		analyseObject(api, gson, user);
		analyseObject(api, gson, repo);
		analyseObject(api, gson, branch);
		analyseObject(api, gson, commit);
		analyseObject(api, gson, tree);
		
		System.out.println("Preparing UI...");
		
		children:
		for (Map.Entry<String, JCallbackDisplay> child: panes.entrySet()) {
			for (Map.Entry<String, JCallbackDisplay> parent: panes.entrySet()) {
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
		
		if (openVisualizer) {
			JFrame frame = new JFrame("GitHub Access Point Visualizer");
			frame.setSize(1420, 940);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JTabbedPane tabs = new JTabbedPane();
			
			for (Map.Entry<String, List<String>> entry: categories.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					JTabbedPane sub = new JTabbedPane();
					
					sub.add("Main", panes.get(entry.getKey()));
					
					for (String child: entry.getValue()) {
						JScrollPane pane = new JScrollPane(panes.get(child));
						pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						
						sub.add(child.split(" | ")[0].replace(entry.getKey().split(" | ")[0], ""), pane);
					}
					
					tabs.add(entry.getKey().split(" | ")[2], sub);
				}
				else {
					JScrollPane pane = new JScrollPane(panes.get(entry.getKey()));
					pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					
					tabs.add(entry.getKey().split(" | ")[2], pane);
				}
			}
			
			frame.add(tabs);
			
			frame.setVisible(true);
		}
	}

	private static void analyseObject(GitHubWebAPI api, Gson gson, GitHubObject object) {
		System.out.println("Scanning '" + object.getURL() + "'...");
		JsonElement element = object.getRawResponseAsJson();
		
		if (element == null) {
			System.err.println("Connection failed.");
			System.out.println(object.getURL());
			System.exit(0);
		}
		
		try {
			colorize(api, gson, object, "", "", element);
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
		
		JCallbackDisplay text = new JCallbackDisplay(builder.toString());
		
		String label = object.getURL() + " | " + object.getClass().getSimpleName().replace("GitHub", "") + (element.isJsonArray() ? "[]": "");
		
		panes.put(label, text);
	}
	
	private static void colorize(GitHubWebAPI api, Gson gson, GitHubObject object, String path, String dir, JsonElement element) throws Exception {
		if (element.isJsonArray()) {
			JsonArray array = element.getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				colorize(api, gson, object, path, dir, array.get(i));
			}
		}
		else if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			Map<String, GitHubAccessPoint> queries = getSubURLs(dir, object);
			
			Map<String, JsonElement> content = new HashMap<String, JsonElement>();
			
			for (Map.Entry<String, JsonElement> json: obj.entrySet()) {
				boolean colored = false;
				
				String p = path + (path == "" ? "": "/") + json.getKey();
				
				if (json.getValue().isJsonPrimitive()) {
					query:
					for (Map.Entry<String, GitHubAccessPoint> entry: queries.entrySet()) {
						String url = entry.getKey().split(" | ")[0];
						String regex = entry.getKey().split(" | ")[2];
						
						if (url.contains("@")) {
							String attribute = url.split("@")[1];
							if (attribute.equals(p)) {
								colored = true;
								break query;
							}
						}
						else {
							Pattern pattern = Pattern.compile(regex);
							final Matcher matcher = pattern.matcher(json.getValue().getAsString());
							if (matcher.matches()) {
								colored = true;
								
								if (entry.getValue() != null) {
									Class<?> c = entry.getValue().type();
									
									if (json.getValue().getAsString().contains(url) && GitHubObject.class.isAssignableFrom(c)) {
										Constructor<?> constructor = getConstructor(c);
										
										if (constructor != null) {
											GitHubObject o = new GitHubObject(api, null, url);
											
											try {
												analyseObject(api, gson, (GitHubObject) constructor.newInstance(o));
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								
								break query;
							}
						}
					}
				}
				else {
					query:
					for (Map.Entry<String, GitHubAccessPoint> entry: queries.entrySet()) {
						String url = entry.getKey().split(" | ")[0];
						
						if (url.contains("@")) {
							String attribute = url.split("@")[1];
							
							if (attribute.equals(p)) {
								if (entry.getValue() != null) {
									Class<?> c = entry.getValue().type();
									
									if (GitHubObject.class.isAssignableFrom(c)) {
										Constructor<?> constructor = getConstructor(c);
										
										if (constructor != null) {
											GitHubObject o = new GitHubObject(api, null, object.getURL());
											GitHubObject dummy = (GitHubObject) constructor.newInstance(o);
											
											colorize(api, gson, dummy, p, p, json.getValue());
										}
									}
								}
								
								break query;
							}
							else if (attribute.contains("/") && attribute.startsWith(p + "/")) {
								colorize(api, gson, object, p, dir, json.getValue());
								break query;
							}
						}
					}
				}
				
				if (colored) {
					content.put("\"GITHUB_ACCESS_POINT\"" + json.getKey(), json.getValue());
				}
				else {
					content.put(json.getKey(), json.getValue());
				}
			}
			
			List<String> keys = new ArrayList<String>(content.keySet());
			
			Collections.sort(keys, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					if (o1.contains("\"GITHUB_ACCESS_POINT\"") && !o2.contains("\"GITHUB_ACCESS_POINT\"")) {
						return -1;
					}
					else if (!o1.contains("\"GITHUB_ACCESS_POINT\"") && o2.contains("\"GITHUB_ACCESS_POINT\"")) {
						return 1;
					}
					else {
						return 0;
					}
				}
			});
			
			for (String key: keys) {
				if (key.contains("\"GITHUB_ACCESS_POINT\"")) {
					obj.remove(key.replace("\"GITHUB_ACCESS_POINT\"", ""));
				}
				else {
					obj.remove(key);
				}
				obj.add(key, content.get(key));
			}
		}
	}

	public static Map<String, GitHubAccessPoint> getSubURLs(String path, GitHubObject object) {
		Map<String, GitHubAccessPoint> queries = new HashMap<String, GitHubAccessPoint>();
		
	    Class<?> c = object.getClass();
	    
	    while (c != GitHubObject.class) {
	        for (final Method method : c.getDeclaredMethods()) {
	            if (method.isAnnotationPresent(GitHubAccessPoint.class)) {
	                Annotation annotation = method.getAnnotation(GitHubAccessPoint.class);
	            	String p = ((GitHubAccessPoint) annotation).path();
	            	
	            	if (p.startsWith("@")) {
	            		p = p.replace("@", "@" + (path == "" ? "": (path + "/")));
	            	}
	            	
	                queries.put(object.getURL() + p + " | " + object.getRawURL() + p + ".*", (GitHubAccessPoint) annotation);
	            }
	        }
	        
	        c = c.getSuperclass();
	    }
	    
	    String p = "@" + (path == "" ? "": (path + "/")) + "url";
        queries.put(object.getURL() + p + " | " + object.getRawURL() + p + ".*", null);
	    
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
	
	public static class JCallbackDisplay extends JEditorPane {
		
		private static final long serialVersionUID = -5137159745987700918L;
		
		public String raw;
		
		public JCallbackDisplay(String raw) {
			this.raw = raw;
			
			setContentType("text/html");
			setBackground(new Color(30, 30, 30));
			setText(raw);
			setEditable(false);
		}
	}

}
