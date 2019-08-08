package in.sskrishna.gatekeeper.provider;

import in.sskrishna.gatekeeper.model.MyGit;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class GitNativeUtil {
    private final MyGit myGit;

    public GitNativeUtil(MyGit myGit) {
        this.myGit = myGit;
    }

    private static final String LATEST_COMMIT_CMD = "git log {branch} --format=\"%H\" -n 1\n";
    private static final String ALL_COMMIT_CMD = "git log {branch} --format=\"%H\"\n";
    private static final String BRANCH_COMMIT_COUNT_CMD = "git rev-list --count {branch}\n";
    private static final String TOTAL_COMMITS_COUNTS_CMD = "git rev-list --all --count\n";
    private static final String DISK_SIZE = "du -shm {dir}";
    private static final String BARE_REPO = "git rev-parse --is-bare-repository \n";


    public boolean isBareRepo() throws IOException, InterruptedException {
        String result = this.execute(this.BARE_REPO);
        return result.equals("true");
    }

    public List<String> getAllCommitIds(String branchName) throws IOException, InterruptedException {
        String cmd = this.ALL_COMMIT_CMD.replace("{branch}", branchName);
        return this.executeMultiLine(cmd);
    }

    public String getLatestCommit(String branchName) throws IOException, InterruptedException {
        String cmd = this.LATEST_COMMIT_CMD.replace("{branch}", branchName);
        return this.execute(cmd);
    }

    public int getTotalCommits(String branchName) throws IOException, InterruptedException {
        String cmd = this.BRANCH_COMMIT_COUNT_CMD.replace("{branch}", branchName);
        return Integer.valueOf(this.execute(cmd));
    }

    public int getTotalCommits() throws IOException, InterruptedException {
        String cmd = this.TOTAL_COMMITS_COUNTS_CMD;
        return Integer.valueOf(this.execute(cmd));
    }

    public String execute(String cmd) throws IOException, InterruptedException {
        return this.executeMultiLine(cmd).get(0);
    }

    public Integer getDiskUsage() throws IOException, InterruptedException {
        if (SystemUtils.IS_OS_WINDOWS) {
            return -1;
        }
        String cmd = this.DISK_SIZE.replace("{dir}", this.myGit.getLocalDir());
        String result = this.execute(cmd);
        String size = result.split("\t")[0];
        return Integer.valueOf(size);
    }

    public List<String> executeMultiLine(String cmd) throws IOException, InterruptedException {
        List<String> lines = new LinkedList<>();
        Process process = Runtime.getRuntime().exec(cmd, null, new File(this.myGit.getLocalDir()));
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            line = line.replace("\"", "");
            lines.add(line);
        }
        process.waitFor();
        return lines;
    }
}