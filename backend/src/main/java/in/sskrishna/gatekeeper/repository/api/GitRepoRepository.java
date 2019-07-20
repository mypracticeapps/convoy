package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.GitRepo;

import java.util.Set;

public interface GitRepoRepository {
    void save(GitRepo repo);

    void delete(GitRepo repo);

    Set<GitRepo> findAll();

    GitRepo findOne(String id);

    long size();
}
