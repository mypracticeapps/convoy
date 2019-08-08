package in.sskrishna.gatekeeper.provider;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.model.MyGit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class GitProviderImpl implements GitProvider {

    private final MyGit myGit;
    private final GitNativeUtil gitNative;

    private Set<String> branchNames;
    private Set<MyGit.Branch> branches;
    private Map<String, Commit> commitMap;

    public GitProviderImpl(MyGit myGit) {
        this.myGit = myGit;
        this.gitNative = new GitNativeUtil(myGit);
    }

    private Git open() throws IOException {
        return Git.open(new File(this.myGit.getLocalDir()));
    }

    private UsernamePasswordCredentialsProvider getCredentials() {
        return new UsernamePasswordCredentialsProvider(this.myGit.getSecret(), "");
    }

    private Set<String> listBranchNames() throws IOException, GitAPIException {
        if (branchNames != null) return this.branchNames;

        try (Git git = this.open()) {
            Set<String> branchSet = new HashSet<>();
            List<Ref> branchRefList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref branchRef : branchRefList) {
                String name = branchRef.getName().replace("refs/heads/", "");
                branchSet.add(name);
            }
            this.branchNames = branchSet;
            return this.branchNames;
        }
    }

    private Map<String, Commit> readCommits(Iterable<RevCommit> commitItr) {
        Map<String, Commit> commitMap = new HashMap<>();
        commitItr.forEach((commitRef) -> {
            Commit commit = new Commit();
            commit.setId(commitRef.getId().getName());
            commit.setRepoId(this.myGit.getId());
            commit.setMessage(commitRef.getShortMessage());
            for (RevCommit parentRef : commitRef.getParents()) {
                String id = parentRef.getId().getName();
                commit.addParentId(id);
            }
            commit.setAuthor(this.toPerson(commitRef.getAuthorIdent()));
            commit.setCommitter(this.toPerson(commitRef.getCommitterIdent()));
            commitMap.put(commit.getId(), commit);
        });
        return commitMap;
    }

    private Commit.Person toPerson(PersonIdent pi) {
        Commit.Person person = new Commit.Person();

        person.setName(pi.getName());
        person.setEmail(pi.getEmailAddress());

        ZoneId zoneId = pi.getTimeZone().getDefault().toZoneId();
        LocalDateTime ldt = LocalDateTime.ofInstant(pi.getWhen().toInstant(),
                zoneId);

        person.setTime(ldt);
        return person;
    }

    private void buildSortOrderNext() throws InterruptedException, GitAPIException, IOException {
        for (MyGit.Branch branch : this.getBranches()) {
            List<String> sortedCommitIds = this.gitNative.getAllCommitIds(branch.getName());
            this.linkSortedCommits(sortedCommitIds);
        }
    }

    private void linkSortedCommits(List<String> sortedCommitIds) throws InterruptedException, GitAPIException, IOException {
        Map<String, Commit> commitMap = this.getCommits();
        String previousCommitId = null;
        for (String currentCommitId : sortedCommitIds) {
            if (previousCommitId == null) {
                previousCommitId = currentCommitId;
                continue;
            }
            Commit previousCommit = commitMap.get(previousCommitId);
            Commit currentCommit = commitMap.get(currentCommitId);
            if (previousCommit.getSortOrderNext() == null) {
                previousCommit.setSortOrderNext(currentCommit.getId());
                previousCommitId = currentCommitId;
            } else {
                break;
            }
        }
    }

    public boolean exists() {
        try {
            Git.open(new File(this.myGit.getLocalDir())).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void assertBareRepo() throws IOException, InterruptedException {
        if (!this.gitNative.isBareRepo()) {
            throw new RuntimeException("This git repo is not a bare repo. pleas make sure it is base repo if you are manually cloning it");
        }
    }

    public void cloneGit() throws GitAPIException, IOException {
        Git git = Git.cloneRepository()
                .setBare(true)
                .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
                .setCredentialsProvider(this.getCredentials())
                .setURI(this.myGit.getUrl())
                .setDirectory(new File(this.myGit.getLocalDir()))
                .setCloneAllBranches(true)
                .call();
        git.close();
    }

    public void fetch() throws GitAPIException, IOException, InterruptedException {
        try (Git git = this.open()) {
            List<RemoteConfig> remotes = git.remoteList().call();
            for (RemoteConfig remote : remotes) {
                git
                        .fetch()
                        .setCheckFetchedObjects(true)
                        .setCredentialsProvider(this.getCredentials())
                        .setForceUpdate(true)
                        .setRemote(remote.getName())
                        .setRefSpecs(remote.getFetchRefSpecs())
                        .call();
            }

            this.branchNames = null;
            branches = null;
            commitMap = null;
        }
    }

    public Set<MyGit.Branch> getBranches() throws IOException, GitAPIException, InterruptedException {
        if (this.branches != null) return this.branches;

        Set<String> branchNames = this.listBranchNames();
        Set<MyGit.Branch> branches = new HashSet<>();
        for (String branchName : branchNames) {
            String latestCommitId = this.gitNative.getLatestCommit(branchName);
            int size = this.gitNative.getTotalCommits(branchName);
            MyGit.Branch branch = new MyGit.Branch(branchName, latestCommitId, size);
            branches.add(branch);
        }
        this.branches = branches;
        return this.branches;
    }

    public Map<String, Commit> getCommits() throws IOException, GitAPIException, InterruptedException {
        if (this.commitMap != null) return this.commitMap;
        try (Git git = this.open()) {
            Iterable<RevCommit> commitItr = git.log().all().call();
            this.commitMap = this.readCommits(commitItr);
            Assert.assertEquals(this.commitMap.size(), this.gitNative.getTotalCommits());
            this.buildSortOrderNext();
            return commitMap;
        }
    }
}
