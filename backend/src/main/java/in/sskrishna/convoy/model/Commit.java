package in.sskrishna.convoy.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Commit extends Entity{
    private String repoId;
    private String parentId;
    private String message;
    private Person author;
    private Person committer;
//    private String userStory;

    @Data
    public static class Person {
        private String name;
        private String email;
        private LocalDateTime time;
    }
}
