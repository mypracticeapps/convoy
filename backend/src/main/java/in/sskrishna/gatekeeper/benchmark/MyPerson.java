package in.sskrishna.gatekeeper.benchmark;

import in.sskrishna.gatekeeper.model.Entity;
import io.codearte.jfairy.producer.person.Person;
import lombok.Data;

import java.util.UUID;

@Data
public class MyPerson extends Entity {
    private Person person;

    public MyPerson(Person person) {
        super.id = UUID.randomUUID().toString();
        this.person = person;
    }
}