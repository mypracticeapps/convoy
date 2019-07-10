package in.sskrishna.convoy.provider;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface GitProvider {
    boolean exits(GitRepo repo);

    void clone(GitRepo repo) throws GitAPIException;

    void fetch(GitRepo repo) throws GitAPIException, IOException;

    Set<GitRepo.Branch> getBranches(GitRepo repo) throws IOException, GitAPIException;

    Map<String, Commit> listCommits(GitRepo repo) throws IOException, GitAPIException;
}