package project.hierarchies.instances;

import project.hierarchies.Instance;

import java.util.UUID;

public abstract class Peripheral extends Instance {

    public Peripheral() {
        super();
    }

    public Peripheral(UUID identifier) {
        super(identifier);
    }

}
