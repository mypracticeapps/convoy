package in.sskrishna.convoy.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommitSet {
    private String repoId;
    private String branchName;

    public CommitSet(String repoId, String branchName){
        this.repoId = repoId;
        this.branchName = branchName;
    }

    private List<Commit> commitList = new ArrayList<>();

    public String getId() {
        return this.repoId + "/" + branchName;
    }
}