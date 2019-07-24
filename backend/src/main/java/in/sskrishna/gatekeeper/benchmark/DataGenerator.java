package in.sskrishna.gatekeeper.benchmark;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DataGenerator {
    public static Set<MyPerson> createPersons() {
        Set<MyPerson> personSet = new HashSet<>();
        Fairy fairy = Fairy.create();
        for (int ii = 0; ii < 5_00_00; ii++) {
            personSet.add(new MyPerson(fairy.person()));
        }
        return personSet;
    }


}