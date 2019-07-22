package in.sskrishna.gatekeeper.provider;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.model.GitRepo;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface GitProvider {
    public boolean exists();

    public void assertBareRepo() throws GitAPIException, IOException, InterruptedException;

    public void cloneGit() throws GitAPIException, IOException;

    public void fetch() throws GitAPIException, IOException, InterruptedException;

    public Set<GitRepo.Branch> getBranches() throws IOException, GitAPIException, InterruptedException;

    public Map<String, Commit> getCommits() throws IOException, GitAPIException, InterruptedException;
}
