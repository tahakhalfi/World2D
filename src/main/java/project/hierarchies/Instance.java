package project.hierarchies;

import project.services.ReplicationService;

import java.util.*;

public class Instance {

    private static final Map<UUID, Instance> INSTANCES = new HashMap<>();

    private final UUID identification;

    private Instance parent;
    private final List<Instance> children = new LinkedList<>();

    private String name;

    private static void add(UUID identification, Instance instance) {
        INSTANCES.put(identification, instance);
    }

    private static void sub(UUID identification) {
        INSTANCES.remove(identification);
    }

    private static void sub(Instance instance) {
        sub(instance.getIdentification());
    }

    public static Instance get(UUID identification) {
        return INSTANCES.get(identification);
    }

    public static UUID get(Instance instance) {
        return instance.getIdentification();
    }

    public static boolean cont(UUID identification) {
        return INSTANCES.containsKey(identification);
    }

    public static boolean cont(Instance instance) {
        return cont(instance.getIdentification());
    }

    public static <C extends Instance> C create(Class<C> clazz) {
        return create(
                UUID.randomUUID(),
                clazz
        );
    }

    public static <C extends Instance> C create(UUID identification, Class<C> clazz) {

        if (INSTANCES.containsKey(identification)) {
            throw new IllegalArgumentException(
                    "The field 'identification' has to be unique."
            );
        }

        C instance = switch (clazz.getSimpleName()) {

            case "Instance" -> clazz.cast(new Instance(identification));

            default -> throw new IllegalArgumentException(
                    "The field 'clazz' has to be or extend the Instance class."
            );

        };

        Instance.add(
                identification,
                instance
        );

        ReplicationService.configurateCreation(
                identification,
                clazz
        );

        return instance;

    }

    private static void destroy(Instance instance) {

        for (Instance child: instance.children) {
            destroy(child);
        }

        instance.subParent();
        instance.children.clear();

        UUID identification = instance.getIdentification();

        Instance.sub(
                identification
        );

        ReplicationService.configurateDestruction(
                identification
        );

    }

    private static void destroy(UUID identification) {
        Instance instance = Instance.get(identification);
        if (instance != null) {
            Instance.destroy(instance);
        }
    }

    private Instance(UUID identification) {
        this.identification = identification;
    }

    public void destroy() {
        Instance.destroy(this);
    }

    public UUID getIdentification() {
        return this.identification;
    }

    public void setParent(Instance parent) {
        this.subParent();
        this.addParent(parent);
    }

    private void addParent(Instance parent) {
        if (parent != null) {
            this.parent = parent;
            this.parent.addChild(this);
        }
    }

    private void subParent() {
        if (this.parent != null) {
            this.parent.subChild(this);
            this.parent = null;
        }
    }

    public Instance getParent() {
        return this.parent;
    }

    public <C> C getParent(Class<C> clazz) {
        if (clazz.isInstance(this.parent)) {
            return clazz.cast(this.parent);
        }
        return null;
    }

    private void addChild(Instance child) {
        this.children.add(child);
    }

    private void subChild(Instance child) {
        this.children.remove(child);
    }

    public List<Instance> getChildren() {
        return new ArrayList<>(this.children);
    }

    public <C> List<C> getChildren(Class<C> clazz) {
        List<C> children = new ArrayList<>();
        for (Instance child: this.children) {
            if (clazz.isInstance(child)) {
                children.add(clazz.cast(child));
            }
        }
        return children;
    }

    public Instance findChild(String name) {
        for (Instance child: this.children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public <C> C findChild(Class<C> clazz, String name) {
        for (Instance child: this.children) {
            if (clazz.isInstance(child) && child.getName().equals(name)) {
                return clazz.cast(child);
            }
        }
        return null;
    }

    public List<Instance> getDescendants() {
        List<Instance> descendants = new ArrayList<>();
        for (Instance child: this.children) {
            descendants.add(child);
            descendants.addAll(child.getDescendants());
        }
        return descendants;
    }

    public <C> List<C> getDescendants(Class<C> clazz) {
        List<C> descendants = new ArrayList<>();
        for (Instance child: this.children) {
            if (clazz.isInstance(child)) {
                descendants.add(clazz.cast(child));
                descendants.addAll(child.getDescendants(clazz));
            }
        }
        return descendants;
    }

    public Instance findDescendant(String name) {
        for (Instance child: this.getDescendants()) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public <C> C findDescendant(Class<C> clazz, String name) {
        for (Instance child: this.getDescendants()) {
            if (clazz.isInstance(child) && child.getName().equals(name)) {
                return clazz.cast(child);
            }
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "[" + this.identification + "] : " + this.name;
    }

}
