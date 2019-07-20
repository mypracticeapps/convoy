package in.sskrishna.gatekeeper.service.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class GitRepoLoader implements ApplicationRunner {

    private final GitRepoRepository repository;

    @Value("${gatekeeper.workingdir}")
    private String workingDir;

    public GitRepoLoader(GitRepoRepository repository) throws IOException {
        this.repository = repository;
    }

    public List<GitRepo> load() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ClassPathResource resource = new ClassPathResource("repositories.yml");
        TypeReference<List<GitRepo>> tRef = new TypeReference<List<GitRepo>>() {
        };

        List<GitRepo> repoSet = mapper.readValue(resource.getInputStream(), tRef);
        repoSet.forEach((conf -> {
            conf.setName(getRepoName(conf.getUrl()));
            conf.setOwner(getOwner(conf.getUrl()));
            conf.setId(conf.getOwner() + "/" + conf.getName());
            conf.setLocalDir(this.workingDir + "/" + conf.getName());
        }));
        return repoSet;
    }

    public String getOwner(String url) {
        return this.parse(url)[1];
    }

    public String getRepoName(String url) {
        String name = this.parse(url)[2];
        name = name.replace(".git", "");
        return name;
    }

    public String[] parse(String url) {
        UrlDetector parser = new UrlDetector(url, UrlDetectorOptions.Default);
        Url found = parser.detect().get(0);
        String sub[] = found.getPath().split("/");
        return sub;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.load().forEach(gitRepo -> repository.save(gitRepo));
        log.info("loaded {} git repo configs", this.repository.size());
    }
}
