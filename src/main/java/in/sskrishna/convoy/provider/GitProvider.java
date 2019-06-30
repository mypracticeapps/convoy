package in.sskrishna.convoy.provider;

import in.sskrishna.convoy.model.GitRepo;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface GitProvider {
    public boolean exits(GitRepo repo);
    public void clone(GitRepo repo) throws GitAPIException;
    public void fetch(GitRepo repo) throws GitAPIException, IOException;
}
