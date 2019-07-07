package in.sskrishna.convoy.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Commit {
    private String id;
    private String shortSha;
    private String parentId;
    private String message;
    private Person author;
    private Person committer;
//    private String userStory;

    public void setId(String id){
        this.shortSha = id.substring(0,7);
    }

    @Data
    public static class Person {
        private String name;
        private String email;
        private LocalDateTime time;
    }
}
