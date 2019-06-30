package in.sskrishna.convoy.provider;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import in.sskrishna.convoy.model.GitRepo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class GitProviderImpl implements GitProvider {

//    @Override
//    public void getCommits(Repository repository) {
//
//    }

    public static void main(String args[]) throws IOException, GitAPIException {
//        client.setOAuth2Token("");

//        HttpResponse<JsonNode> response = Unirest
//                .get("https://api.github.com/repos/jenkinsci/jenkins/branches")
//                .header("Authorization", "Token 0451eba6750c42a352f1a3590b0753bec4569904")
//                .asJson();
//
//        JsonNode body = response.getBody();
//        BranchParser.parse(body);

//        Git git = clone("jgit");
        Git git = open("jgit");
//        fetchAll(git);
//        listBranchs(git);
        listAllCommits(git, "test");
    }

    private static Git clone(String name) throws GitAPIException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("0451eba6750c42a352f1a3590b0753bec4569904", "");

        Git git = Git.cloneRepository()
                .setBare(true)
                .setCredentialsProvider(credentialsProvider)
                .setURI("https://github.com/srikrishna-s/" + name + ".git")
                .setDirectory(new File("/tmp/jgit/" + name))
                .setCloneAllBranches(true)
                .call();
        return git;
    }

    private static Git open(String name) throws IOException {
        return Git
                .open(new File("/tmp/jgit/" + name));
    }

    private static void fetchAll(Git git) throws GitAPIException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("0451eba6750c42a352f1a3590b0753bec4569904", "");
        List<RemoteConfig> remotes = git.remoteList().call();
        for (RemoteConfig remote : remotes) {
            git.fetch().setCredentialsProvider(credentialsProvider)
                    .setForceUpdate(true)
                    .setRemote(remote.getName())
                    .setRefSpecs(remote.getFetchRefSpecs())
                    .call();
        }
    }

    private static void listBranchs(Git git) throws GitAPIException {
        List<Ref> branchRefList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        for (Ref branchRef : branchRefList) {
            String name = branchRef.getName().substring(branchRef.getName().lastIndexOf("/") + 1, branchRef.getName().length());
            System.out.println(name);
        }
    }

    private static void listAllCommits(Git git, String branchName) throws IOException, GitAPIException {
        for (RevCommit commitRef : git.log().all().call()) {
            System.out.println(commitRef.getName() + " : " + commitRef.getShortMessage() + " : " + commitRef.getAuthorIdent());
        }
    }

    @Override
    public boolean exits(GitRepo repo) {
        try {
            Git.open(new File(repo.getLocalDir())).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void clone(GitRepo repo) throws GitAPIException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(repo.getSecret(), "");

        Git git = Git.cloneRepository()
                .setBare(true)
                .setCredentialsProvider(credentialsProvider)
                .setURI(repo.getUrl())
                .setDirectory(new File(repo.getLocalDir()))
                .setCloneAllBranches(true)
                .call();
        git.close();
    }

    @Override
    public void fetch(GitRepo repo) throws GitAPIException, IOException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(repo.getSecret(), "");
        Git git = Git.open(new File(repo.getLocalDir()));

        List<RemoteConfig> remotes = git.remoteList().call();
        for (RemoteConfig remote : remotes) {
            git.fetch().setCredentialsProvider(credentialsProvider)
                    .setForceUpdate(true)
                    .setRemote(remote.getName())
                    .setRefSpecs(remote.getFetchRefSpecs())
                    .call();
        }
    }
}
