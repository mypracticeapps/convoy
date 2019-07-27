package in.sskrishna.gatekeeper.benchmark.mongo;

import in.sskrishna.gatekeeper.benchmark.DataGenerator;
import in.sskrishna.gatekeeper.benchmark.MyPerson;
import in.sskrishna.gatekeeper.config.repo.MongoConfig;
import in.sskrishna.gatekeeper.repository.mongo.MongoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@SpringBootApplication
@Import({MongoConfig.class})
public class MongoBenchmark {

    public static void main(String[] args) {
        SpringApplication.run(MongoBenchmark.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(MongoTemplate template) {
        return args -> {
            Set<MyPerson> persons = DataGenerator.createPersons(1_00_000);
            MongoUtil mUtil = new MongoUtil(template, MyPerson.class, null);
            mUtil.deleteAll();
            log.info("starting benchmark insertion");
            ZonedDateTime taskNow = ZonedDateTime.now();
            mUtil.saveAll(persons);
            log.info("time taken for insert bulk entities: {} size: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS), persons.size());
        };
    }
}
