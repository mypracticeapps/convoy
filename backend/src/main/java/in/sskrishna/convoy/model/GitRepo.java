package in.sskrishna.convoy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.sskrishna.rest.response.ErrorDetail;
import lombok.Data;

import java.util.*;

@Data
public class GitRepo {
    private String id;
    private String name;
    private String owner;
    private String url;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secret;
    private String localDir;
    private Set<Branch> branches = new HashSet();

    private Status status = new Status();

    @Data
    public static class Branch {
        private String name;
        private String latestCommitId;
        private int totalCommits;

        public Branch(String branchName, String id, int size) {
            this.name = branchName;
            this.latestCommitId = id;
            this.totalCommits = size;
        }
    }

    @Data
    public static class Status {
        private boolean initialized = true;
        private Set<ErrorDetail> warnings = new HashSet<>();
        private Set<ErrorDetail> errors = new HashSet<>();

        @JsonIgnore
        private transient List<String> initStatusErrors =
                Arrays.asList(new String[]{"repo.initialization.failed", "repo.invalid.remote"});

        public void addWarning(ErrorDetail errorDetail) {
            this.warnings.add(errorDetail);
        }

        public void addError(ErrorDetail errorDetail) {
            if (this.initStatusErrors.contains(errorDetail.getCode()))
                this.initialized = false;
            this.errors.add(errorDetail);
        }

        public void removeWarning(String code) {
            this.removeErrorDetail(this.warnings, code);
        }

        public void removeError(String code) {
            if (this.initStatusErrors.contains(code))
                this.initialized = true;
            this.removeErrorDetail(this.errors, code);
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