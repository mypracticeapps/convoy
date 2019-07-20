package in.sskrishna.gatekeeper.repository;

import in.sskrishna.gatekeeper.model.Entity;
import org.springframework.stereotype.Service;

@Service
public class CommitMappingRepo<String, CommitMapping extends Entity> extends CacheRepo<java.lang.String, CommitMapping> {

}
