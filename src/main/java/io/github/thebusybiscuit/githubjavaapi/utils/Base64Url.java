package io.github.thebusybiscuit.githubjavaapi.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.annotation.Nonnull;

public class Base64Url {

    private Base64Url() {}

    public static @Nonnull String encode(@Nonnull String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes).replace('/', '_');
    }

    public static @Nonnull String decode(@Nonnull String base64) {
        byte[] bytes = base64.replace('_', '/').getBytes(StandardCharsets.UTF_8);
        return new String(Base64.getDecoder().decode(bytes));
    }

}
