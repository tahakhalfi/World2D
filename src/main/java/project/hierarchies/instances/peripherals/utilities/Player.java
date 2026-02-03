package project.hierarchies.instances.peripherals.utilities;

import project.hierarchies.instances.Peripheral;

import java.util.UUID;

public class Player extends Peripheral {

    private UUID reference;

    public Player() {
        super();
    }

    public Player(UUID identifier) {
        super(identifier);
    }

    public Player duplicate() {
        throw new IllegalCallerException("Players cannot be duplicated.");
    }

    public void setReference(UUID reference) {
        this.reference = reference;
    }

    public UUID getReference() {
        return this.reference;
    }

}
