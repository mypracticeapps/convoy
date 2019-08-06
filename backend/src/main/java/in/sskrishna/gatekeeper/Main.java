package in.sskrishna.gatekeeper;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;

public class Main {
    public static void main(String args[]) {
//        CountDownLatch latch = new CountDownLatch(1);
//        latch.countDown();
//        System.out.println(latch.getCount());
        benchmark();
    }

    public static void benchmark() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        System.out.println(fairy.person().getFirstName());
        System.out.println(fairy.person().getFullName());
    }
}
