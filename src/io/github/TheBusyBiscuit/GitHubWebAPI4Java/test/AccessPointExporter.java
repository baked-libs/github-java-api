package io.github.TheBusyBiscuit.GitHubWebAPI4Java.test;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AccessPointExporter {
	
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		AccessPointVisualizer.run(false);
		
		System.out.println("Exporting files...");
		
		try {
			FileWriter writer = new FileWriter("E:\\Projects\\GitHub\\TheBusyBiscuit.github.io\\docs\\GitHubWebAPI4Java\\api.json");
			
			JsonArray settings = new JsonArray();
			
			int i = 0;
			for (Map.Entry<String, List<String>> entry: AccessPointVisualizer.categories.entrySet()) {
				JsonObject category = new JsonObject();
				category.addProperty("name", entry.getKey().split(" | ")[2]);
				category.addProperty("file", i + ".txt");
				category.addProperty("url", entry.getKey().split(" | ")[0]);
				
				FileWriter text = new FileWriter("E:\\Projects\\GitHub\\TheBusyBiscuit.github.io\\docs\\GitHubWebAPI4Java\\callbacks\\" + i + ".txt");
				text.write(AccessPointVisualizer.panes.get(entry.getKey()).raw);
				i++;
				
				JsonArray children = new JsonArray();
				
				for (String child: entry.getValue()) {
					JsonObject obj = new JsonObject();
					
					obj.addProperty("url", child.split(" | ")[0].replace(entry.getKey().split(" | ")[0], ""));
					obj.addProperty("file", i + ".txt");
					
					FileWriter text2 = new FileWriter("E:\\Projects\\GitHub\\TheBusyBiscuit.github.io\\docs\\GitHubWebAPI4Java\\callbacks\\" + i + ".txt");
					text2.write(AccessPointVisualizer.panes.get(child).raw);

					text2.flush();
					text2.close();
					
					children.add(obj);
					i++;
				}
				
				category.add("children", children);
				
				text.flush();
				text.close();
				
				settings.add(category);
			}
			
			gson.toJson(settings, writer);
			
			writer.flush();
			writer.close();
		}
		catch(Exception x) {
			x.printStackTrace();
			System.exit(0);
		}
	}

}
