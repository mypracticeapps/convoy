package in.sskrishna.convoy.model;

import lombok.Data;

@Data
public class Commit {
    private String id;
    private String parentId;
    private String message;
//    private String userStory;
//    private GitUser author;
//    private GitUser committer;
}
