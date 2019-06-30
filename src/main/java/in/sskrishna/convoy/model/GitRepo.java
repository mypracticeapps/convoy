package in.sskrishna.convoy.model;

import lombok.Data;

@Data
public class GitRepo {
    private String id;
    private String name;
    private String owner;
    private String url;
    private String secret;
    private String localDir;
}
