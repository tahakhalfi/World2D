package project.hierarchies.instances.peripherals.elements;

import project.hierarchies.instances.peripherals.Element;

import java.util.UUID;

public class View extends Element {

    public View() {
        super();
    }

    public View(UUID identifier) {
        super(identifier);
    }

    public View duplicate() {
        return new View();
    }

}
