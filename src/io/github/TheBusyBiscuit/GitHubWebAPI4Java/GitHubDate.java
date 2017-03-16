package io.github.TheBusyBiscuit.GitHubWebAPI4Java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GitHubDate {
	
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Date parse(String str) {
		try {
			return date_format.parse(str.replace("T", " ").replace("Z", ""));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
