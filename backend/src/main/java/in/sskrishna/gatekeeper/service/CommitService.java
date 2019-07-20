package in.sskrishna.gatekeeper.service;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import in.sskrishna.gatekeeper.validators.CommitValidator;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CommitService {
    private final RestErrorBuilder errorBuilder;
    private final CommitValidator commitValidator;
    private final GitRepoRepository gitRepository;
    private final CommitRepo commitRepository;

    public CommitService(RestErrorBuilder errorBuilder,
                         CommitValidator commitValidator,
                         GitRepoRepository gitRepository,
                         CommitRepo commitRepository) {
        this.errorBuilder = errorBuilder;
        this.gitRepository = gitRepository;
        this.commitRepository = commitRepository;
        this.commitValidator = commitValidator;
    }

    public List<Commit> getCommits(String repoId, String fromCommitId, int size) {
        this.commitValidator.validateGetCommits(repoId, fromCommitId, size);
        List<Commit> commitList = new LinkedList<>();
        for (int ii = 0; ii < size; ii++) {
            if (fromCommitId == null) {
                break;
            }
            Commit nextCommit = (Commit) this.commitRepository.findOne(fromCommitId);
            commitList.add(nextCommit);
            fromCommitId = nextCommit.getSortOrderNext();
        }
        return commitList;
    }
}
