package in.sskrishna.gatekeeper;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String args[]) {
        CountDownLatch latch = new CountDownLatch(1);
        latch.countDown();
        System.out.println(latch.getCount());
    }
}
