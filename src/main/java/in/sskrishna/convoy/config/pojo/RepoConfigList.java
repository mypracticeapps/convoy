package in.sskrishna.convoy.config.pojo;

import in.sskrishna.convoy.model.GitRepo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RepoConfigList {
    private List<GitRepo> repositories = new ArrayList<>();
}
