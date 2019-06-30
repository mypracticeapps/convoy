package in.sskrishna.convoy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

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
