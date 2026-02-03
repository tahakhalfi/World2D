package project.hierarchies.instances.peripherals;

import project.hierarchies.instances.Peripheral;
import project.primitives.Udimor;

import java.util.HashMap;
import java.util.UUID;

public abstract class Exhibit extends Peripheral {

    private Udimor dimension;
    private Udimor location;
    private double rotation;

    public Exhibit() {
        super();
    }

    public Exhibit(UUID identifier) {
        super(identifier);
    }

    protected void configure() {
        super.configure();
        this.dimension = new Udimor(1);
        this.location = new Udimor();
        this.rotation = 0.0;
    }

    public void setDimension(Udimor dimension) {
        this.dimension = dimension;
    }

    public Udimor getDimension() {
        return this.dimension;
    }

    public void setLocation(Udimor location) {
        this.location = location;
    }

    public Udimor getLocation() {
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
