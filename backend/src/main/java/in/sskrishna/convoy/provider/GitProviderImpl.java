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
import java.util.stream.Collectors;
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
            git
                    .fetch()
                    .setCheckFetchedObjects(true)
                    .setCredentialsProvider(credentialsProvider)
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

    @Override
    public Map<String, Commit> listCommits(GitRepo repo) throws IOException, GitAPIException {
        Repository repoInterface = new FileRepository(repo.getLocalDir());
        Git git = new Git(repoInterface);

        Iterable<RevCommit> commitItr = git.log().all().call();
        Map<String, Commit> commitMap = this.readCommits(commitItr, repo);
        this.buildChildGraph(repo, commitMap);
        return commitMap;
    }

    private void buildChildGraph(GitRepo repo, Map<String, Commit> commitMap) throws IOException, GitAPIException {
        Set<GitRepo.Branch> branches = this.getBranches(repo);
        Stack<Pair> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        Set<String> commitIdSet = commitMap.values().stream().map(commit -> commit.getId()).collect(Collectors.toSet());

        for (GitRepo.Branch branch : branches) {
            Pair pair = new Pair(branch.getLatestCommitId(), null);
            stack.push(pair);
        }

        while (!stack.isEmpty()) {
            Pair currentPair = stack.pop();

            String currentCommitId = currentPair.getParentId();
            String childCommitId = currentPair.getChildId();

            if (currentCommitId != null) {
                Commit currentCommit = commitMap.get(currentCommitId);
                currentCommit.addChildId(childCommitId);

                if (!visited.contains(currentCommitId)) {
                    for (String parentId : currentCommit.getParentIds()) {
                        Pair pair = new Pair(parentId, currentCommitId);
                        stack.push(pair);
                    }
                }

                visited.add(currentCommitId);
            }
        }
        // will be useful to debug when tag feature is implemented
//        System.out.println("Total: " + commitIdSet.size() + " Visited: " + visited.size() + " Stack size: " + stack.size());
//        for (String diff : this.difference(commitIdSet, visited)) {
//            System.out.println("Commit diff: " + diff);
//        }
//        Assert.assertEquals(visited.size(), commitMap.values().size(), " expected and actual does not match");
    }

    private Commit getLatestCommit(Iterator<Commit> commits) {
        Commit latestCommit = StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(commits, Spliterator.ORDERED), false)
                .sorted(this.commitComparator())
                .findFirst()
                .get();
        return latestCommit;
    }

    private Map<String, Commit> listCommits(GitRepo repo, String branch) throws IOException, GitAPIException {
        Repository repoInterface = new FileRepository(repo.getLocalDir());
        Git git = new Git(repoInterface);

        ObjectId objectId = repoInterface.resolve("refs/heads/" + branch);
        Iterable<RevCommit> commitItr = git.log().add(objectId).call();
        return this.readCommits(commitItr, repo);
    }

    private Map<String, Commit> readCommits(Iterable<RevCommit> commitItr, GitRepo repo) {
        Map<String, Commit> commitMap = new HashMap<>();
        commitItr.forEach((commitRef) -> {
            Commit commit = new Commit();
            commit.setId(commitRef.getId().getName());
            commit.setRepoId(repo.getId());
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

    private static class Pair extends javafx.util.Pair<String, String> {
        Pair(String parent, String child) {
            super(parent, child);
        }

        String getParentId() {
            return super.getKey();
        }

        String getChildId() {
            return super.getValue();
        }
    }

    public Set<String> difference(final Set<String> set1, final Set<String> set2) {
        final Set<String> larger = set1.size() > set2.size() ? set1 : set2;
        final Set<String> smaller = larger.equals(set1) ? set2 : set1;
        return larger.stream().filter(n -> !smaller.contains(n)).collect(Collectors.toSet());
    }
}