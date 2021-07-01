package io.github.thebusybiscuit.githubjavaapi.extra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GitHubDate {

    public static Date parse(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str.replace("T", " ").replace("Z", ""));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
