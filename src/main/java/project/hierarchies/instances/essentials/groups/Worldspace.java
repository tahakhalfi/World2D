package project.hierarchies.instances.essentials.groups;

import project.hierarchies.instances.Essential;

import java.util.UUID;

public class Worldspace extends Essential {

    // CLASS FUNCTIONS

    public static Worldspace get() {
        return Essential.get(Worldspace.class);
    }

    // INSTANCE FUNCTIONS

    public Worldspace() {
        super();
    }

    public Worldspace(UUID identifier) {
        super(identifier);
    }

}
