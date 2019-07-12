package in.sskrishna.convoy.validators;

import in.sskrishna.convoy.repository.GitRepoRepository;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

@Service
public class RepoActionValidator {
    private final RestErrorBuilder errorBuilder;
    private final GitRepoRepository gitRepository;

    public RepoActionValidator(RestErrorBuilder errorBuilder,
                               GitRepoRepository gitRepository) {
        this.errorBuilder = errorBuilder;
        this.gitRepository = gitRepository;
    }
}
