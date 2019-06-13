package io.github.TheBusyBiscuit.GitHubWebAPI4Java.test;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.RepositoryFeature.State;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubBranch;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubCommit;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubFileTree;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubIssue;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubMilestone;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubPullRequest;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubRepository;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubOrganization;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

public class AccessPointVisualizer {
	
	public static Map<String, String> data = new HashMap<>();
	public static Map<String, List<String>> categories = new HashMap<>();
	
	public static Map<Class<?>, List<String>> blacklist = new HashMap<>();
	
	private static void setupBlacklist() {
		blacklist.put(GitHubUser.class, Arrays.asList("plan", "two_factor_authentication"));
		blacklist.put(GitHubCommit.class, Arrays.asList("commit/committer", "commit/author"));
		blacklist.put(GitHubRepository.class, Arrays.asList("forks", "watchers", "open_issues", "permissions", "trees_url"));
		blacklist.put(GitHubIssue.class, Arrays.asList("assignee", "labels_url"));
		blacklist.put(GitHubPullRequest.class, Arrays.asList("assignee", "_links/issue", "_links/comments", "_links/commits"));
	}
	
	public static void main(String[] args) throws IOException {
		run(true);
	}
	
	protected static void run(boolean openVisualizer) throws IOException {
		setupBlacklist();
		
		String token = new String(Files.readAllBytes(Paths.get("E:\\Projects\\Eclipse\\GitHubWebAPI4Java\\ACCESS_TOKEN")), StandardCharsets.UTF_8);
		
		GitHubWebAPI api = new GitHubWebAPI(token);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		
		GitHubUser user = api.getUser("TheBusyBiscuit");
		GitHubRepository repo = user.getRepository("Slimefun4");
		GitHubOrganization org = api.getOrganization("Bukkit");
		GitHubBranch branch = null;
		GitHubCommit commit = null;
		GitHubFileTree tree = null;
		GitHubPullRequest pr = null;
		GitHubIssue issue = null;
		GitHubMilestone milestone = null;
		
		try {
			branch = repo.getDefaultBranch();
			commit = branch.getLastCommit();
			tree = commit.getFileTree();
			
			pr = repo.getPullRequests().get(0);
			issue = repo.getIssues(State.OPEN).get(0);
			milestone = repo.getMilestones().get(0);
		} catch (IllegalAccessException e) {
			System.err.println("Connection failed.");
			System.exit(0);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Encoding failed.");
			System.exit(0);
		}
		
		analyseObject(api, gson, user);
		analyseObject(api, gson, repo);
		analyseObject(api, gson, branch);
		analyseObject(api, gson, commit);
		analyseObject(api, gson, tree);
		analyseObject(api, gson, issue);
		analyseObject(api, gson, pr);
		analyseObject(api, gson, milestone);
		analyseObject(api, gson, org);
		
		System.out.println("Preparing UI...");
		
		for (Map.Entry<String, String> child: data.entrySet()) {
			System.out.println(child.getKey());
			
			String parent = findDominantParent(child.getKey());
			
			if (!parent.equals("")) {
				List<String> list = new ArrayList<String>();
				if (categories.containsKey(parent)) {
					list = categories.get(parent);
				}
				list.add(child.getKey());
				categories.put(parent, list);
				
				continue;
			}
			
			if (!categories.containsKey(child.getKey())) {
				categories.put(child.getKey(), new ArrayList<String>());
			}
		}
		
		List<String> keys = new ArrayList<String>(categories.keySet());
		
		Collections.sort(keys, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if (!o1.split(" | ")[2].equals("User") && o2.split(" | ")[2].equals("User")) {
					return 1;
				}
				else if (!o1.split(" | ")[2].equals("Repository") && o2.split(" | ")[2].equals("Repository")) {
					return 1;
				}
				else if (o1.split(" | ")[2].equals("User") && !o2.split(" | ")[2].equals("User")) {
					return -1;
				}
				else if (o1.split(" | ")[2].equals("Repository") && !o2.split(" | ")[2].equals("Repository")) {
					return -1;
				}
				else {
					return 0;
				}
			}
		});
		
		if (openVisualizer) {
			JFrame frame = new JFrame("GitHub Access Point Visualizer");
			frame.setSize(1420, 940);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JTabbedPane tabs = new JTabbedPane();
			
			for (String key: keys) {
				List<String> value = categories.get(key);
				if (!value.isEmpty()) {
					JTabbedPane sub = new JTabbedPane();
					
					JScrollPane main =  new JScrollPane(new JCallbackDisplay(data.get(key)));
					main.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					main.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					
					sub.add("Main", main);
					
					for (String child: value) {
						JScrollPane pane = new JScrollPane(new JCallbackDisplay(data.get(child)));
						pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						
						sub.add(child.split(" | ")[0].replace(key.split(" | ")[0], ""), pane);
					}
					
					tabs.add(key.split(" | ")[2], sub);
				}
				else {
					JScrollPane pane = new JScrollPane(new JCallbackDisplay(data.get(key)));
					pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					
					tabs.add(key.split(" | ")[2], pane);
				}
			}
			
			frame.add(tabs);
			
			frame.setVisible(true);
		}
	}
	
	private static String findDominantParent(String key) {
		List<String> family = new ArrayList<String>();
		
		String parent = findNextFamilyMember(key);
		
		while (!parent.equals("")) {
			family.add(parent);
			
			parent = findNextFamilyMember(parent);
		}
		
		if (family.size() > 1) {
			return family.get(family.size() - 2);
		}
		else if (family.size() > 0) {
			return family.get(0);
		}
		else {
			return "";
		}
	}

	private static String findNextFamilyMember(String key) {
		String dominantParent = "";
		
		for (Map.Entry<String, String> parent: data.entrySet()) {
			if (key.equals(parent.getKey())) {
				continue;
			}
			String path_child = key.split(" | ")[0];
			String path_parent = parent.getKey().split(" | ")[0];
			if (path_child.startsWith(path_parent)) {
				if (path_parent.length() > dominantParent.split(" | ")[0].length()) {
					dominantParent = parent.getKey();
				}
			}
		}
		
		return dominantParent;
	}
	
	static int o = 1;

	private static void analyseObject(GitHubWebAPI api, Gson gson, GitHubObject object) {
		analyseObject(api, gson, object, false);
	}

	private static void analyseObject(GitHubWebAPI api, Gson gson, GitHubObject object, boolean requiresAccessToken) {
		System.out.println("(" + o + ") Scanning '" + object.getURL() + "'...");
		o++;
		JsonElement element = object.getRawResponseAsJson();
		
		if (element == null) {
			System.err.println("Connection failed.");
			System.out.println(object.getURL());
			System.exit(0);
		}
		
		try {
			colorize(api, gson, object, "", "", element, requiresAccessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(element).replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;");
		StringBuilder builder = new StringBuilder();
		
		for (String line: json.split("<br>")) {
			if (line.contains("\\\"GITHUB_ACCESS_POINT\\\"")) {
				builder.append("<font color=#44FF44>" + line.replace("\\\"GITHUB_ACCESS_POINT\\\"", "") + "</font><br>");
			}
			else if (line.contains("\\\"GITHUB_AUTHENTICATED_ACCESS_POINT\\\"")) {
				builder.append("<font color=#FFCE00>" + line.replace("\\\"GITHUB_AUTHENTICATED_ACCESS_POINT\\\"", "") + "</font><br>");
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
		
		
		String label = object.getURL() + " | " + object.getClass().getSimpleName().replace("GitHub", "");
		
		data.put(label, builder.toString());
	}
	
	private static void colorize(GitHubWebAPI api, Gson gson, GitHubObject object, String path, String dir, JsonElement element, boolean requiresAccessToken) throws Exception {
		if (element.isJsonArray()) {
			JsonArray array = element.getAsJsonArray();
			
			while (array.size() > 1) {
				array.remove(1);
			}
			
			for (int i = 0; i < array.size(); i++) {
				colorize(api, gson, object, path, dir, array.get(i), requiresAccessToken);
			}
		}
		else if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			
			if (blacklist.containsKey(object.getClass())) {
				for (String hidden: blacklist.get(object.getClass())) {
					String h = (dir == "" ? "": (dir + "/")) + hidden;
					for (Map.Entry<String, JsonElement> json: new HashSet<Map.Entry<String, JsonElement>>(obj.entrySet())) {
						String p = path + (path == "" ? "": "/") + json.getKey();
						
						if (p.equals(h)) {
							obj.remove(json.getKey());
						}
					}
				}
			}
			
			Map<String, GitHubAccessPoint> queries = getSubURLs(dir, object);
			
			Map<String, JsonElement> content = new HashMap<String, JsonElement>();
			
			for (Map.Entry<String, JsonElement> json: obj.entrySet()) {
				GitHubAccessPoint ap = null;
				
				String p = path + (path == "" ? "": "/") + json.getKey();
				
				if (json.getValue().isJsonPrimitive() || json.getValue().isJsonNull()) {
					query:
					for (Map.Entry<String, GitHubAccessPoint> entry: queries.entrySet()) {
						String url = entry.getKey().split(" | ")[0];
						String regex = entry.getKey().split(" | ")[2];
						
						if (url.contains("@")) {
							String attribute = url.split("@")[1];
							if (attribute.equals(p)) {
								ap = entry.getValue();
								break query;
							}
						}
						else if (json.getValue().isJsonPrimitive()) {
							Pattern pattern = Pattern.compile(regex);
							final Matcher matcher = pattern.matcher(json.getValue().getAsString());
							if (matcher.matches()) {
								ap = entry.getValue();
								
								if (entry.getValue() != null) {
									Class<?> c = entry.getValue().type();
									
									if (json.getValue().getAsString().contains(url) && GitHubObject.class.isAssignableFrom(c)) {
										Constructor<?> constructor = getConstructor(c);
										
										if (constructor != null) {
											GitHubObject o = new GitHubObject(api, null, url);
											
											try {
												analyseObject(api, gson, (GitHubObject) constructor.newInstance(o), ap.requiresAccessToken());
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
											
											colorize(api, gson, dummy, p, p, json.getValue(), requiresAccessToken);
										}
									}
								}
								
								break query;
							}
							else if (attribute.contains("/") && attribute.startsWith(p + "/")) {
								colorize(api, gson, object, p, dir, json.getValue(), requiresAccessToken);
								break query;
							}
						}
					}
				}
				
				if (ap != null) {
					if (requiresAccessToken || ap.requiresAccessToken()) {
						content.put("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"" + json.getKey(), json.getValue());
					}
					else {
						content.put("\"GITHUB_ACCESS_POINT\"" + json.getKey(), json.getValue());
					}
				}
				else {
					content.put(json.getKey(), json.getValue());
				}
			}
			
			List<String> keys = new ArrayList<String>(content.keySet());
			
			Collections.sort(keys, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					if (!o1.contains("\"GITHUB_ACCESS_POINT\"") && o2.contains("\"GITHUB_ACCESS_POINT\"")) {
						return 1;
					}
					else if (!o1.contains("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"") && o2.contains("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"")) {
						return 1;
					}
					else if (o1.contains("\"GITHUB_ACCESS_POINT\"") && !o2.contains("\"GITHUB_ACCESS_POINT\"")) {
						return -1;
					}
					else if (o1.contains("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"") && !o2.contains("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"")) {
						return -1;
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
				else if (key.contains("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"")) {
					obj.remove(key.replace("\"GITHUB_AUTHENTICATED_ACCESS_POINT\"", ""));
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
	    
	    while (c != Object.class) {
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
