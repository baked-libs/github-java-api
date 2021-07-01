package io.github.thebusybiscuit.githubjavaapi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GitHubAccessPoint {

    String path();

    Class<?> type();

    boolean requiresAccessToken();

}
