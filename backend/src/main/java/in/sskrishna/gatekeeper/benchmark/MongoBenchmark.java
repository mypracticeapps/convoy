package in.sskrishna.gatekeeper.benchmark;

import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.mongodb.MongoClient;
import in.sskrishna.gatekeeper.repository.mongo.MongoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@Component
public class MongoBenchmark implements ApplicationRunner {
    private final MongoTemplate template;

    public MongoBenchmark(MongoTemplate template) {
        this.template = template;
    }

    public static void main(String args[]) throws Exception {
        MongoTemplate template = new MongoTemplate(new MongoClient("127.0.0.1", 32768), "test");
        MongoBenchmark benchmark = new MongoBenchmark(template);
        benchmark.run(null);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<MyPerson> persons = DataGenerator.createPersons();
        MongoUtil mUtil = new MongoUtil(template, MyPerson.class, null);
        ZonedDateTime taskNow = ZonedDateTime.now();
        mUtil.saveAll(persons);
        log.info("time taken for insert bulk entities: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));
    }
}