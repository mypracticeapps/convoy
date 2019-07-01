package in.sskrishna.convoy;

import in.sskrishna.convoy.service.StartupService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConvoyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvoyApplication.class, args);
    }

	@Bean
	public ApplicationRunner applicationRunner(StartupService startupService){
		return (args)->{
			startupService.startUp();
		};
	}

}