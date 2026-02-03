package project.hierarchies.instances.essentials.groups;

import project.hierarchies.instances.Essential;

import java.util.UUID;

public class Scenespace extends Essential {

    // CLASS FUNCTIONS

    public static Scenespace get() {
        return Essential.get(Scenespace.class);
    }

    // INSTANCE FUNCTIONS

    public Scenespace() {
        super();
    }

    public Scenespace(UUID identifier) {
        super(identifier);
    }

}
