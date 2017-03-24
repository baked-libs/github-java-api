package io.github.TheBusyBiscuit.GitHubWebAPI4Java.extra;

import java.util.ArrayList;
import java.util.List;

import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubCommit;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubFile;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubRepository;
import io.github.TheBusyBiscuit.GitHubWebAPI4Java.GitHubUser;

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
		List<String> files = new ArrayList<String>();
		
		for (GitHubFile file: this.commit.getFileTree().getFiles()) {
			files.add(file.getFile());
		}
		
		return files;
	}

}
