package in.sskrishna.convoy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class GitRepo {
    private String id;
    private String name;
    private String owner;
    private String url;
    private String secret;
    private String localDir;

    private Set<Branch> branches = new HashSet();

    public void addBranch(Branch branch){
        this.branches.add(branch);
    }

    @Data
    @AllArgsConstructor
    public static class Branch {
        private String name;
        private String latestCommitId;
    }
}
