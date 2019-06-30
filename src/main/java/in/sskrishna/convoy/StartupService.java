package in.sskrishna.convoy;

import in.sskrishna.convoy.config.pojo.RepoConfigList;
import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.provider.GitProvider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StartupService {

    private final RepoConfigList repoConfigList;
    private final GitProvider gitProvider;

    @Autowired
    public StartupService(RepoConfigList repoConfigList, GitProvider gitProvider) {
        this.repoConfigList = repoConfigList;
        this.gitProvider = gitProvider;
    }

    public void startUp() throws GitAPIException {
        log.info("Found {} repositories", this.repoConfigList.getRepositories().size());

        for (GitRepo repository : this.repoConfigList.getRepositories()) {
            if (gitProvider.exits(repository)) {
                log.info("repo: {} exists", repository.getId());
            } else {
                log.info("repo: {} does not exists. attempting to clone", repository.getId());
                gitProvider.clone(repository);
            }

            log.info("repo: {} trying to fetch", repository.getId());
        }
    }
}
