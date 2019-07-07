package in.sskrishna.convoy.repository;

import in.sskrishna.convoy.model.Entity;
import org.springframework.stereotype.Service;

@Service
public class CommitRepository<String, Commit> extends CacheRepo {
}