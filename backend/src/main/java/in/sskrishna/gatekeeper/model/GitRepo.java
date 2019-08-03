package in.sskrishna.gatekeeper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.sskrishna.rest.response.ErrorDetail;
import lombok.Data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
public class GitRepo extends Entity {
    private String name;
    private String owner;
    private String url;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secret;
    private String localDir;
    private Set<Branch> branches = new HashSet();
    private int totalCommits;
    private int diskUsage;

    private Status status = new Status();

    @Data
    public static class Branch {
        private String name;
        private String latestCommitId;
        private int totalCommits;

        public Branch(String branchName, String latestCommitId, int size) {
            this.name = branchName;
            this.latestCommitId = latestCommitId;
            this.totalCommits = size;
        }
    }

    @Data
    public static class Status {
        private long lastRefreshedAt;
        private Progress progress;
        private boolean initialized = true;
        private Set<ErrorDetail> warnings = new HashSet<>();
        private Set<ErrorDetail> errors = new HashSet<>();

        public enum Progress {
            QUEUED, IN_PROGRESS, ERROR, DONE
        }

        public void addWarning(ErrorDetail errorDetail) {
            this.warnings.add(errorDetail);
        }

        public void addError(ErrorDetail errorDetail) {
            this.initialized = false;
            this.errors.add(errorDetail);
        }

        public void removeWarning(String code) {
            this.removeErrorDetail(this.warnings, code);
        }

        public void removeError(String code) {
            this.removeErrorDetail(this.errors, code);
            if (this.errors.isEmpty()) {
                this.initialized = true;
            }
        }

        private void removeErrorDetail(Set<ErrorDetail> errorDetails, String code) {
            Iterator<ErrorDetail> iterator = errorDetails.iterator();
            while (iterator.hasNext()) {
                ErrorDetail detail = iterator.next();
                if (detail.getCode().equals(code)) {
                    iterator.remove();
                }
            }
        }

        public boolean hasWarnings() {
            return this.warnings.size() > 0;
        }

        public boolean hasErrors() {
            return this.errors.size() > 0;
        }
    }
}
