package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.CommitSet;
import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.provider.GitProvider;
import in.sskrishna.convoy.repository.CommitRepository;
import in.sskrishna.convoy.repository.GitRepoRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Assert;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class GitService {
    private final GitRepoRepository gitRepository;
    private final CommitRepository commitRepository;
    private final GitProvider gitProvider;

    public GitService(GitRepoRepository gitRepository,
                      CommitRepository commitRepository,
                      GitProvider gitProvider) {
        this.gitRepository = gitRepository;
        this.commitRepository = commitRepository;
        this.gitProvider = gitProvider;
    }

    public void refresh(GitRepo repo) throws GitAPIException, IOException {
        ZonedDateTime now = ZonedDateTime.now();

        Assert.assertTrue(this.gitProvider.exits(repo));
        log.info("updating git repo: " + repo.getId());
        this.gitProvider.fetch(repo);

        log.info("updating branch info for :" + repo.getId());

        Set<String> branchNames = gitProvider.listBranches(repo);
        for (String branchName : branchNames) {
            String latestCommit = this.gitProvider.getLatestCommit(repo, branchName);
            GitRepo.Branch branch = new GitRepo.Branch(branchName, latestCommit);
            repo.addBranch(branch);
        }
        this.gitRepository.save(repo);

        log.info("updating commit history on repo: " + repo.getId());
        Map<String, Commit> commitMap = this.gitProvider.listCommits(repo);
        for (GitRepo.Branch branch : repo.getBranches()) {
            List<Commit> list = new LinkedList<>();
            String latestCommit = branch.getLatestCommitId();
            while (latestCommit != null) {
                Commit commit = commitMap.get(latestCommit);
                list.add(commit);
                latestCommit = commit.getParentId();
            }
            CommitSet commitSet = new CommitSet(repo.getId(), branch.getName());
            commitSet.setCommitList(list);
            this.commitRepository.save(commitSet);
        }

        long seconds = now.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
        log.info("refresh finshed for repo{}, duration: {}", repo.getId(), seconds);
    }
}