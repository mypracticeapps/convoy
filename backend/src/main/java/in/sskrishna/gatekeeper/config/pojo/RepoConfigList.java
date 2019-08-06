package in.sskrishna.gatekeeper.config.pojo;

import in.sskrishna.gatekeeper.model.MyGit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RepoConfigList {
    private List<MyGit> repositories = new ArrayList<>();
}
