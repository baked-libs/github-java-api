package io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubWebAPI;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.annotations.GitHubAccessPoint;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.GitHubObject;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

public class GitHubCollaborator extends GitHubUser {

	public GitHubCollaborator(GitHubWebAPI api, String username, JsonElement response) {
		super(api, username, response);
	}

	public GitHubCollaborator(GitHubObject obj) {
		super(obj);
	}

	@GitHubAccessPoint(path = "@permissions/admin", type = Boolean.class, requiresAccessToken = true)
	public boolean hasAdminPermissions() throws IllegalAccessException {
		if (minimal == null) {
			throw new IllegalAccessException("Invalid GitHubCollaborator Instance.");
		}
		
		JsonObject response = minimal.getAsJsonObject().get("permissions").getAsJsonObject();

		return isInvalid(response, "admin") ? null: response.get("admin").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@permissions/push", type = Boolean.class, requiresAccessToken = true)
	public boolean hasPushPermissions() throws IllegalAccessException {
		if (minimal == null) {
			throw new IllegalAccessException("Invalid GitHubCollaborator Instance.");
		}
		
		JsonObject response = minimal.getAsJsonObject().get("permissions").getAsJsonObject();

		return isInvalid(response, "push") ? null: response.get("push").getAsBoolean();
	}

	@GitHubAccessPoint(path = "@permissions/pull", type = Boolean.class, requiresAccessToken = true)
	public boolean hasPullPermissions() throws IllegalAccessException {
		if (minimal == null) {
			throw new IllegalAccessException("Invalid GitHubCollaborator Instance.");
		}
		
		JsonObject response = minimal.getAsJsonObject().get("permissions").getAsJsonObject();

		return isInvalid(response, "pull") ? null: response.get("pull").getAsBoolean();
	}

}
