package project.hierarchies.instances.essentials;

import project.hierarchies.instances.Essential;

import java.util.UUID;

public class Experience extends Essential {

    // CLASS FUNCTIONS

    public static Experience get() {
        return Essential.get(Experience.class);
    }

    // INSTANCE FUNCTIONS

    public Experience() {
        super();
    }

    public Experience(UUID identifier) {
        super(identifier);
    }

}
