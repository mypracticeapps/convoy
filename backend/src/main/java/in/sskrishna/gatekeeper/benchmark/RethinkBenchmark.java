package in.sskrishna.gatekeeper.benchmark;

import in.sskrishna.gatekeeper.repository.rethink.RethinkUtil;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

//@Component
@Slf4j
public class RethinkBenchmark implements ApplicationRunner {

    private final RethinkUtil rUtil;

    RethinkBenchmark(RethinkUtil rUtil) {
        this.rUtil = rUtil;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String TABLE_NAME = "persons";
        rUtil.createTableIfNotExists(TABLE_NAME, null);
        Set<MyPerson> persons = createPersons();
        rUtil.saveAll(TABLE_NAME, persons);

        ZonedDateTime taskNow = ZonedDateTime.now();
        persons.stream().map(person -> person.getId()).forEach(id -> {
            try {
                rUtil.findOne(TABLE_NAME, id, MyPerson.class);
            } catch (Exception e) {
                // do nothing
            }
        });
        log.info("time taken for read bulk entities: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));
    }

    private Set<MyPerson> createPersons() {
        Set<MyPerson> personSet = new HashSet<>();
        Fairy fairy = Fairy.create();
        for (int ii = 0; ii < 25_00_00; ii++) {
            personSet.add(new MyPerson(fairy.person()));
        }
        return personSet;
    }

    @Data
    public static class MyPerson {
        private String id;
        private Person person;

        public MyPerson(Person person) {
            this.id = UUID.randomUUID().toString();
            this.person = person;
        }
    }
}