package in.sskrishna.gatekeeper.benchmark.rethink;

import com.rethinkdb.net.Connection;
import in.sskrishna.gatekeeper.benchmark.DataGenerator;
import in.sskrishna.gatekeeper.benchmark.MyPerson;
import in.sskrishna.gatekeeper.config.repo.RethinkConfig;
import in.sskrishna.gatekeeper.repository.rethink.RethinkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Import({RethinkConfig.class})
public class RethinkBenchmark {

    public static void main(String[] args) {
        SpringApplication.run(RethinkBenchmark.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(@Qualifier("rethink") Connection connection) {
        return args -> {
            String TABLE_NAME = "persons";
            RethinkUtil rUtil = new RethinkUtil(connection);
            rUtil.createTableIfNotExists(TABLE_NAME, null);
            rUtil.deleteAll(TABLE_NAME);
            Set<MyPerson> persons = DataGenerator.createPersons(1_00_000);

            log.info("starting benchmark insertion");
            ZonedDateTime taskNow = ZonedDateTime.now();
            rUtil.saveAll(TABLE_NAME, persons);
            log.info("time taken for insert bulk entities: {} size: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS), persons.size());
        };
    }
}
