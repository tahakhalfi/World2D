package project.hierarchies.instances.peripherals;

import project.hierarchies.instances.Peripheral;
import project.primitives.Vector;

import java.util.HashMap;
import java.util.UUID;

public abstract class Element extends Peripheral {

    private Vector dimension;
    private Vector location;
    private double rotation;

    public Element() {
        super();
    }

    public Element(UUID identifier) {
        super(identifier);
    }

    protected void configure() {
        super.configure();
        this.dimension = new Vector(1);
        this.location = new Vector();
        this.rotation = 0.0;
    }

    public void setDimension(Vector dimension) {
        this.dimension = dimension;
    }

    public Vector getDimension() {
        return this.dimension;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public Vector getLocation() {
        return this.location;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return this.rotation;
    }

    public HashMap<String, Object> getDigitaly() {
        HashMap<String, Object> digitaly = super.getDigitaly();
        digitaly.put("dimension", this.getDimension());
        digitaly.put("location", this.getLocation());
        digitaly.put("rotation", this.getRotation());
        return digitaly;
    }

}
