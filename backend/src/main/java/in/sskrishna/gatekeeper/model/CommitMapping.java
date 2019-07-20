package in.sskrishna.gatekeeper.model;

import lombok.Data;

import java.util.List;

@Data
public class CommitMapping extends Entity {
    private String repoId;
    private String branchName;
    private List<String> commitIds;
}
