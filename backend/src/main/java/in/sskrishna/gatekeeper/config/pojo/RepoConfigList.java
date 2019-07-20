package in.sskrishna.gatekeeper.config.pojo;

import in.sskrishna.gatekeeper.model.GitRepo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RepoConfigList {
    private List<GitRepo> repositories = new ArrayList<>();
}
