package in.sskrishna.convoy.provider;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class GitProviderImpl implements GitProvider {

    public Git open(GitRepo repo) throws IOException {
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

    private Set<String> listBranchNames(GitRepo repo) throws IOException, GitAPIException {
        Git git = this.open(repo);
        Set<String> branchSet = new HashSet<>();
        List<Ref> branchRefList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        for (Ref branchRef : branchRefList) {
            String name = branchRef.getName().replace("refs/heads/", "");
            branchSet.add(name);
        }
        return branchSet;
    }

    @Override
    public Set<GitRepo.Branch> getBranches(GitRepo repo) throws IOException, GitAPIException {
        Set<String> branchSet = this.listBranchNames(repo);
        Set<GitRepo.Branch> branches = new HashSet<>();
        for (String branchName : branchSet) {
            Map<String, Commit> commitMap = this.listCommits(repo, branchName);
            Commit latestCommit = this.getLatestCommit(commitMap.values().iterator());
            int size = commitMap.size();
            GitRepo.Branch branch = new GitRepo.Branch(branchName, latestCommit.getId(), size);
            branches.add(branch);
        }
        return branches;
    }

    private Commit getLatestCommit(Iterator<Commit> commits) {
        Commit latestCommit = StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(commits, Spliterator.ORDERED), false)
                .sorted(this.commitComparator())
                .findFirst()
                .get();
        return latestCommit;
    }

    @Override
    public Map<String, Commit> listCommits(GitRepo repo, String branch) throws IOException, GitAPIException {
        Repository repoInterface = new FileRepository(repo.getLocalDir());
        Git git = new Git(repoInterface);

        ObjectId objectId = repoInterface.resolve("refs/heads/" + branch);
        Iterable<RevCommit> commitItr = git.log().add(objectId).call();
        return this.readCommits(commitItr, repo);
    }

    @Override
    public Map<String, Commit> listCommits(GitRepo repo) throws IOException, GitAPIException {
        Repository repoInterface = new FileRepository(repo.getLocalDir());
        Git git = new Git(repoInterface);

        Iterable<RevCommit> commitItr = git.log().all().call();
        return this.readCommits(commitItr, repo);
    }

    private Map<String, Commit> readCommits(Iterable<RevCommit> commitItr, GitRepo repo) {
        Map<String, Commit> commitMap = new HashMap<>();
        commitItr.forEach((commitRef) -> {
            Commit commit = new Commit();
            commit.setId(commitRef.getId().getName());
            commit.setRepoId(repo.getId());
            commit.setMessage(commitRef.getShortMessage());
            if (commitRef.getParentCount() >= 1) {
                String id = commitRef.getParent(0).getName();
                commit.setParentId(id);
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

    private Comparator<Commit> commitComparator() {
        Comparator<Commit> commitComparator = new Comparator<Commit>() {
            @Override
            public int compare(Commit commit, Commit t1) {
                if (commit.getCommitter().getTime().isBefore(t1.getCommitter().getTime())) {
                    return 1;
                } else if (commit.getCommitter().getTime().isAfter(t1.getCommitter().getTime())) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        return commitComparator;
    }
}