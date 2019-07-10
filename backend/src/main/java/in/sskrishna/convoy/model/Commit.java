package in.sskrishna.convoy.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.TreeSet;

@Data
public class Commit extends Entity {
    private String repoId;

    // need to have order so that we can build predictable pagination on commits
    private TreeSet<String> parentIds = new TreeSet<>();
    private TreeSet<String> childIds = new TreeSet<>();

    private String message;
    private Person author;
    private Person committer;
//    private String userStory;

    public void addParentId(String id) {
        if (id == null || id.equals("")) return;
        if (!this.parentIds.contains(id))
            this.parentIds.add(id);
    }

    public void addChildId(String id) {
        if (id == null || id.equals("")) return;
        if (!this.childIds.contains(id))
            this.childIds.add(id);
    }

    @Data
    public static class Person {
        private String name;
        private String email;
        private LocalDateTime time;
    }
}
