package in.sskrishna.gatekeeper.util;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Timer {
    private Timer timer;
    private ZonedDateTime now = ZonedDateTime.now();

    private Timer() {

    }

    public static Timer start() {
        return new Timer();
    }

    public long diff() {
        return now.until(ZonedDateTime.now(), ChronoUnit.MILLIS);
    }
}
