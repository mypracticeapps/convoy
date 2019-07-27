package in.sskrishna.gatekeeper.benchmark;

import io.codearte.jfairy.Fairy;

import java.util.HashSet;
import java.util.Set;

public class DataGenerator {
    public static Set<MyPerson> createPersons(int size) {
        Set<MyPerson> personSet = new HashSet<>();
        Fairy fairy = Fairy.create();
        for (int ii = 0; ii < size; ii++) {
            personSet.add(new MyPerson(fairy.person()));
        }
        return personSet;
    }

    public static Set<MyPerson> createPersons() {
        return createPersons(5_00_00);
    }
}
