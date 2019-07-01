package in.sskrishna.convoy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class BranchSet {
    private String repoId;
    private Set<Branch> branches = new HashSet<>();

    public BranchSet(String repoId) {
        this.repoId = repoId;
    }

    public String getId() {
        return this.repoId;
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    @Data
    @AllArgsConstructor
    public static class Branch {
        private String repoId;
        private String name;
        private String latestCommitId;

        public String getId() {
            return repoId + "/" + name;
        }
    }
}