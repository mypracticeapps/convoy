package in.sskrishna.convoy.model;

import io.sskrishna.rest.response.ErrorDetail;
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

    private Set<ErrorDetail> warrnings = new HashSet<>();
    private Set<ErrorDetail> errors = new HashSet<>();

    public void addWarning(ErrorDetail errorDetail) {
        this.warrnings.add(errorDetail);
    }

    public void addError(ErrorDetail errorDetail) {
        this.warrnings.add(errorDetail);
    }

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