package in.sskrishna.convoy.model;

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

    @Data
    public static class Branch {
        private String name;
        private String latestCommitId;
        private int totalCommits;

        public Branch(String branchName, String id, int size) {
            this.name = branchName;
            this.latestCommitId = id;
            this.totalCommits = size;
        }
    }
}
