package project.hierarchies.instances.peripherals.elements;

import project.hierarchies.instances.peripherals.Element;

import java.util.UUID;

public class Block extends Element {

    public Block() {
        super();
    }

    public Block(UUID identifier) {
        super(identifier);
    }

    @Override
    protected Block duplicate() {
        return new Block();
    }

}
