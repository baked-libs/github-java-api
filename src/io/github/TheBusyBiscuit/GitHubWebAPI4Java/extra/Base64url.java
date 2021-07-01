package io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra;

import javax.xml.bind.DatatypeConverter;

public class Base64url {

    public static String encode(String text) {
        return DatatypeConverter.printBase64Binary(text.getBytes()).replaceAll("/", "_");
    }

    public static String decode(String base64) {
        return new String(DatatypeConverter.parseBase64Binary(base64.replaceAll("_", "/")));
    }

}
