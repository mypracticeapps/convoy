package in.sskrishna.convoy.provider;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Service
public class GitProviderImpl implements GitProvider {

    private Git open(GitRepo repo) throws IOException {
        return Git.open(new File(repo.getLocalDir()));
    }

    @Override
    public boolean exits(GitRepo repo) {
        try {
            Git.open(new File(repo.getLocalDir())).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void clone(GitRepo repo) throws GitAPIException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(repo.getSecret(), "");

        Git git = Git.cloneRepository()
                .setBare(true)
                .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
                .setCredentialsProvider(credentialsProvider)
                .setURI(repo.getUrl())
                .setDirectory(new File(repo.getLocalDir()))
                .setCloneAllBranches(true)
                .call();
        git.close();
    }

    @Override
    public void fetch(GitRepo repo) throws GitAPIException, IOException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(repo.getSecret(), "");
        Git git = Git.open(new File(repo.getLocalDir()));

        List<RemoteConfig> remotes = git.remoteList().call();
        for (RemoteConfig remote : remotes) {
            git.fetch().setCredentialsProvider(credentialsProvider)
                    .setForceUpdate(true)
                    .setRemote(remote.getName())
                    .setRefSpecs(remote.getFetchRefSpecs())
                    .call();
        }
    }

    @Override
    public Set<String> listBranches(GitRepo repo) throws IOException, GitAPIException {
        Git git = this.open(repo);
        Set<String> branchSet = new HashSet<>();
        List<Ref> branchRefList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        for (Ref branchRef : branchRefList) {
            String name = branchRef.getName().substring(branchRef.getName().lastIndexOf("/") + 1, branchRef.getName().length());
            branchSet.add(name);
        }
        return branchSet;
    }

    @Override
    // TODO need refactoring or git log shell command
    public String getLatestCommit(GitRepo repo, String branchName) throws IOException, GitAPIException {
        Git git = this.open(repo);

        final List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        final RevWalk revWalk = new RevWalk(git.getRepository());

        String commitId = branches.stream()
                .filter(branch -> branch.getName().endsWith(branchName))
                .map(branch -> {
                    try {
                        return revWalk.parseCommit(branch.getObjectId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sorted(Comparator.comparing((RevCommit commit) -> commit.getAuthorIdent().getWhen()).reversed())
                .findFirst().get().getName();

        return commitId;
    }

    @Override
    public Map<String, Commit> listCommits(GitRepo repo, String branch) throws IOException, GitAPIException {
        Repository repoInterface = new FileRepository(repo.getLocalDir());
        Git git = new Git(repoInterface);

        Iterable<RevCommit> commitItr = git.log().add(repoInterface.resolve("refs/heads/" + branch)).call();
        return this.readCommits(commitItr);

    }

    @Override
    public Map<String, Commit> listCommits(GitRepo repo) throws IOException, GitAPIException {
        Repository repoInterface = new FileRepository(repo.getLocalDir());
        Git git = new Git(repoInterface);

        Iterable<RevCommit> commitItr = git.log().all().call();
        return this.readCommits(commitItr);
    }

    private Map<String, Commit> readCommits(Iterable<RevCommit> commitItr) {
        Map<String, Commit> commitMap = new HashMap<>();
        commitItr.forEach((commitRef) -> {
            Commit commit = new Commit();
            commit.setId(commitRef.getId().getName());
            commit.setMessage(commitRef.getShortMessage());
            if (commitRef.getParentCount() >= 1) {
                String id = commitRef.getParent(0).getName();
                commit.setParentId(id);
            }
            commitMap.put(commit.getId(), commit);
        });
        return commitMap;
    }
}