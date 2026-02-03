package project.hierarchies.instances.essentials.groups;

import project.hierarchies.instances.Essential;

import java.util.UUID;

public class Depotspace extends Essential {

    // CLASS FUNCTIONS

    public static Depotspace get() {
        return Essential.get(Depotspace.class);
    }

    // INSTANCE FUNCTIONS

    public Depotspace() {
        super();
    }

    public Depotspace(UUID identifier) {
        super(identifier);
    }

}
