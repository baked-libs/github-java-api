package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import com.google.gson.JsonElement;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubGist;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;

/**
 * Represents a file or a directory that is part of a {@link GitHubRepository} or a {@link io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubGist}.
 */
public class GitHubFile extends GitHubObject {
	
	private GitHubRepository repo;
	private GitHubGist gist;
	
	public GitHubFile(GitHubWebAPI api, GitHubRepository repo, String suffix) {
		super(api, repo, suffix);
		
		this.repo = repo;
	}
	
	public GitHubFile(GitHubWebAPI api, GitHubRepository repo, String suffix, JsonElement response) {
		super(api, repo, suffix);
		
		this.repo = repo;
		this.minimal = response;
	}

	/**
	 * Constructs a GitHubFile out of a GitHubGist and the name of the file.
	 * @since 1.3.3
	 */
	public GitHubFile(GitHubWebAPI api, GitHubGist gist, String name) {
		super(api, gist, name);

		this.gist = gist;
	}

	public GitHubFile(GitHubObject obj) {
		super(obj);
	}
	
	public GitHubRepository getRepository() {
		return this.repo;
	}

	@GitHubAccessPoint(path = "@sha", type = String.class, requiresAccessToken = false)
	public String getID() throws IllegalAccessException {
		return getString("sha", false);
	}

	@GitHubAccessPoint(path = "@path", type = String.class, requiresAccessToken = false)
	public String getFile() throws IllegalAccessException {
		return getString("path", false);
	}

	@GitHubAccessPoint(path = "@type", type = String.class, requiresAccessToken = false)
	public String getType() throws IllegalAccessException {
		return getString("type", false);
	}
	
	@GitHubAccessPoint(path = "@size", type = Integer.class, requiresAccessToken = false)
	public Integer getSize() throws IllegalAccessException {
		return getInteger("size", false);
	}

	/**
	 * Returns the name of this file, or the provided suffix if none was found.
	 * @return the name of this file.
	 * @throws IllegalAccessException if the connection to the GitHub API could not be established.
	 * @since 1.3.3
	 */
	public String getName() throws IllegalAccessException {
		if (repo != null) {
			return getString("name", false);
		} else if (gist != null) {
			return getString("filename", false);
		}
		return suffix;
	}
}
