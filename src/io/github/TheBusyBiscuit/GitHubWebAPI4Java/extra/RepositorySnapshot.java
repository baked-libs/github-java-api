package io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra;

import java.util.ArrayList;
import java.util.List;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubCommit;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubFile;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.repositories.GitHubRepository;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.objects.users.GitHubUser;

public class RepositorySnapshot {
	
	private GitHubCommit commit;
	
	public RepositorySnapshot(GitHubCommit commit) {
		this.commit = commit;
	}
	
	public GitHubRepository getRepository() {
		return this.commit.getRepository();
	}
	
	public GitHubCommit getCommit() {
		return this.commit;
	}
	
	public String getCommitMessage() throws IllegalAccessException {
		return this.commit.getMessage();
	}
	
	public GitHubUser getAuthor() throws IllegalAccessException {
		return this.commit.getAuthor();
	}
	
	public List<String> getFiles() throws IllegalAccessException {
		List<String> files = new ArrayList<>();
		
		for (GitHubFile file: this.commit.getFileTree().getFiles()) {
			files.add(file.getFile());
		}
		
		return files;
	}

}
