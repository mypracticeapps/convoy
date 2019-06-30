package in.sskrishna.convoy.model;

import lombok.Data;

import java.util.List;

@Data
public class CommitSet {
    private String branchId;
    private List<Commit> commitList;

    public String getId() {
        return this.branchId;
    }
}