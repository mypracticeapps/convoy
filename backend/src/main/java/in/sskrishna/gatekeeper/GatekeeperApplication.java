package in.sskrishna.gatekeeper;

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
	public ApplicationRunner applicationRunner(StartupService startupService){
		return (args)->{
			startupService.startUp();
		};
	}
}
