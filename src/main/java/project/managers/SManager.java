package project.managers;

import project.hierarchies.Instance;
import project.services.ChronologueService;

import java.time.Duration;

public class SManager {

    public void config() {
        this.start();
    }

    public void start() {

        ChronologueService.delay(10);

        Instance i1 = Instance.create(Instance.class);
        Instance i2 = Instance.create(Instance.class);

    }

}
