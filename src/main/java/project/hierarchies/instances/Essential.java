package project.hierarchies.instances;

import project.hierarchies.Instance;

import java.util.HashMap;
import java.util.UUID;

public abstract class Essential extends Instance {

    // CLASS FUNCTIONS

    private static final HashMap<String, Essential> PRIMITIVES = new HashMap<>();

    private static void set(Essential essential) {
        Essential.PRIMITIVES.put(essential.getVariety(), essential);
    }

    public static Essential get(String variety) {
        return Essential.PRIMITIVES.get(variety);
    }

    public static <P extends Essential> P get(Class<P> clazz) {
        return clazz.cast(Essential.get(clazz.getSimpleName()));
    }

    public static boolean check(String variety) {
        return Essential.PRIMITIVES.containsKey(variety);
    }

    public static boolean check(Essential essential) {
        return Essential.check(essential.getVariety());
    }

    public static <P extends Essential> boolean check(Class<P> clazz) {
        return Essential.check(clazz.getSimpleName());
    }

    // INSTANCE FUNCTIONS

    public Essential() {

        super();

        if (Essential.check(this)) {
            throw new IllegalCallerException("Essentials cannot have a clone.");
        }

        Essential.set(this);

    }

    public Essential(UUID identifier) {

        super(identifier);

        if (Essential.check(this)) {
            throw new IllegalCallerException("Essentials cannot have a clone.");
        }

        Essential.set(this);

    }

    protected Instance duplicate() {
        throw new IllegalCallerException("Essentials cannot be duplicated.");
    }
}
