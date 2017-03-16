package io.github.TheBusyBiscuit.GitHubWebAPI4Java.test;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import com.google.gson.JsonElement;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubUser;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;

public class VisualizeGitHubAccessPoints {
	
	private static Map<JScrollPane, String> panes = new HashMap<JScrollPane, String>();
	
	public static void main(String[] args) {
		GitHubWebAPI api = new GitHubWebAPI();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		GitHubUser user = api.getUser("TheBusyBiscuit");
		
		JFrame frame = new JFrame("GitHub Access Point Visualizer");
		frame.setSize(1420, 940);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabs = new JTabbedPane();
		
		analyseObject(api, gson, user);
		analyseObject(api, gson, user.getRepository("Slimefun4"));
		
		for (Map.Entry<JScrollPane, String> entry: panes.entrySet()) {
			tabs.addTab(entry.getValue(), entry.getKey());
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
		
		String json = gson.toJson(element).replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;");
		
		Map<String, Class<?>> queries = getSubURLs(object);
		StringBuilder builder = new StringBuilder();
		
		lines:
		for (String line: json.split("<br>")) {
			for (Map.Entry<String, Class<?>> entry: queries.entrySet()) {
				if (line.contains("\"url\":")) {
					builder.append("<font color=#44FF44>" + line + "</font><br>");
					continue lines;
				}
				else if (entry.getKey().split(" | ")[0].contains("@")) {
					if (line.contains("\"" + entry.getKey().split(" | ")[0].split("@")[1] + "\":")) {
						builder.append("<font color=#44FF44>" + line + "</font><br>");
						continue lines;
					}
				}
				else {
					Pattern pattern = Pattern.compile(entry.getKey().split(" | ")[2]);
					final Matcher matcher = pattern.matcher(line);
					if (matcher.matches()) {
						builder.append("<font color=#44FF44>" + line + "</font><br>");
						
						if (line.contains(entry.getKey().split(" | ")[0]) && GitHubObject.class.isAssignableFrom(entry.getValue())) {
							Constructor<?> constructor = getConstructor(entry.getValue());
							
							if (constructor != null) {
								GitHubObject content = new GitHubObject(api, null, entry.getKey().split(" | ")[0]);
								
								try {
									analyseObject(api, gson, (GitHubObject) constructor.newInstance(content));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						
						continue lines;
					}
				}
			}
			
			builder.append("<font color=#FF4444>" + line + "</font><br>");
		}
		
		JEditorPane text = new JEditorPane();
		text.setContentType("text/html");
		text.setBackground(new Color(30, 30, 30));
		text.setText(builder.toString());

		JScrollPane pane = new JScrollPane(text);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panes.put(pane, object.getClass().getSimpleName().replace("GitHub", ""));
	}

	public static Map<String, Class<?>> getSubURLs(GitHubObject object) {
		Map<String, Class<?>> queries = new HashMap<String, Class<?>>();
		
	    Class<?> c = object.getClass();
	    
	    while (c != GitHubObject.class) {
	        for (final Method method : c.getDeclaredMethods()) {
	            if (method.isAnnotationPresent(GitHubAccessPoint.class)) {
	                Annotation annotation = method.getAnnotation(GitHubAccessPoint.class);
	                queries.put(object.getURL() + ((GitHubAccessPoint) annotation).path() + " | " + object.getRawURL() + ((GitHubAccessPoint) annotation).path() + ".*", ((GitHubAccessPoint) annotation).type());
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
