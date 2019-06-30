package in.sskrishna.convoy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import in.sskrishna.convoy.config.pojo.RepoConfigList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RepoConfLoader {

    @Value("${convoy.workingdir}")
    private String workingDir;

    @Bean
    public RepoConfigList load() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ClassPathResource resource = new ClassPathResource("repositories.yml");
        RepoConfigList configList = mapper.readValue(resource.getFile(), RepoConfigList.class);
        configList.getRepositories().forEach((conf -> {
            conf.setName(getRepoName(conf.getUrl()));
            conf.setOwner(getOwner(conf.getUrl()));
            conf.setId(conf.getOwner() + "/" + conf.getName());
            conf.setLocalDir(this.workingDir + "/" + conf.getName());
        }));
        return configList;
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
}
