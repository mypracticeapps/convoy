package in.sskrishna.gatekeeper;

import in.sskrishna.gatekeeper.repository.rethink.RethinkUtil;
import in.sskrishna.gatekeeper.service.core.StartupService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class GatekeeperApplication {

    public static void main(String[] args) throws IOException, GitAPIException {
        SpringApplication.run(GatekeeperApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(StartupService startupService) {
        return (args) -> {
            startupService.startUp();
        };
    }

//    @Bean
//    public ApplicationRunner applicationRunner(RethinkUtil rUtil) {
//        return (args) -> {
//            rUtil.createTableIfNotExists("git_repos");
////            Map<String, String> test = new HashMap<>();
////            test.put("id", "1");
////            test.put("name", "cj");
////
////            rUtil.save("git_repos", test);
////            test.put("name", "rj");
////            rUtil.save("git_repos", test);
////            System.out.println(rUtil.contains("git_repos", "1"));
////            rUtil.delete("git_repos", "1");
//        };
//    }
}
