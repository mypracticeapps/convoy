package in.sskrishna.gatekeeper.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.TreeSet;

@Data
public class Commit extends Entity {
    private String repoId;

    private TreeSet<String> parentIds = new TreeSet<>();
    private String sortOrderNext;

    private String message;
    private Person author;
    private Person committer;

    public void addParentId(String id) {
        if (id == null || id.equals("")) return;
        if (!this.parentIds.contains(id))
            this.parentIds.add(id);
    }

    @Data
    public static class Person {
        private String name;
        private String email;

        @JsonFormat(pattern = "yyyy-MM-dd:HH-mm-ss")
        private LocalDateTime time;
    }
}
