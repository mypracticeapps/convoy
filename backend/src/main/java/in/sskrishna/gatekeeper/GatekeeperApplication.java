package in.sskrishna.gatekeeper;

import in.sskrishna.gatekeeper.service.core.StartupService;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
}