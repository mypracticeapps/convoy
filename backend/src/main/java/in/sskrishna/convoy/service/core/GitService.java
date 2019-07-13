package in.sskrishna.convoy.service.core;

import in.sskrishna.convoy.model.Commit;
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
import java.util.Map;
import java.util.Set;

//import in.sskrishna.convoy.service.core.locks.RepoRefreshLock;

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
        Set<GitRepo.Branch> branches = gitProvider.getBranches(repo);
        repo.setBranches(branches);
        this.gitRepository.save(repo);

        log.info("updating commit history on repo: " + repo.getId());
        Map<String, Commit> commitMap = this.gitProvider.listCommits(repo);
        this.commitRepository.save(commitMap.values());

        long seconds = now.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
        log.info("refresh finshed for repo{}, duration: {}", repo.getId(), seconds);
    }
}