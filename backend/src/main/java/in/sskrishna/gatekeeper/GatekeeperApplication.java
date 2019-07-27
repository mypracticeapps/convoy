package in.sskrishna.gatekeeper;

import in.sskrishna.gatekeeper.benchmark.mongo.MongoBenchmark;
import in.sskrishna.gatekeeper.benchmark.rethink.RethinkBenchmark;
import in.sskrishna.gatekeeper.service.core.StartupService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MongoBenchmark.class, RethinkBenchmark.class})})
public class GatekeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatekeeperApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(StartupService startupService) {
        return (args) -> {
            startupService.startUp();
        };
    }
}
