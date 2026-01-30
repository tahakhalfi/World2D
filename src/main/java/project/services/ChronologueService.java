package project.services;

import project.hierarchies.Instance;

import java.time.Duration;

public class ChronologueService {

    public static void delay(long lapse) {
        try {
            Thread.sleep(Duration.ofSeconds(lapse));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
