package project.hierarchies;

import project.services.ReplicateService;
import project.utilities.formating.Bundlure;
import project.utilities.formating.Signature;
import project.utilities.signaling.Event;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class Instance {

    private static final Map<UUID, Instance> INSTANCES = new HashMap<>();

    private static final Event<Consumer<Instance>> instanceCreatedEvent = new Event<>();
    private static final Event<Consumer<Instance>> instanceDestroyedEvent = new Event<>();

    // CLASS FUNCTIONS

    private static void insert(UUID identifier, Instance instance) {
        instance.setIdentifier(identifier);
        Instance.INSTANCES.put(identifier, instance);
    }

    private static void insert(Instance instance) {
        Instance.insert(UUID.randomUUID(), instance);
    }

    private static void extract(UUID identifier, Instance instance) {
        instance.setIdentifier(null);
        Instance.INSTANCES.remove(identifier);
    }

    private static void extract(Instance instance) {
        Instance.extract(instance.getIdentifier(), instance);
    }

    public static Instance obtain(UUID identifier) {
        return Instance.INSTANCES.get(identifier);
    }

    public static UUID identify(Instance instance) {
        return (instance != null) ? instance.getIdentifier() : null;
    }

    public static boolean check(UUID identifier) {
        return Instance.INSTANCES.containsKey(identifier);
    }

    public static boolean check(Instance instance) {
        return Instance.check(instance.getIdentifier());
    }

    public static void onInstanceCreated(Consumer<Instance> function) {
        Instance.instanceCreatedEvent.connect(function);
    }

    public static void onInstanceDestroyed(Consumer<Instance> function) {
        Instance.instanceDestroyedEvent.connect(function);
    }

    // INSTANCE FUNCTIONS

    private UUID identifier;

    protected Instance parent = null;
    private final List<Instance> children = new ArrayList<>();

    private final Event<Consumer<Instance>> childAddedEvent = new Event<>();
    private final Event<Consumer<Instance>> childSubbedEvent = new Event<>();

    private final Event<Consumer<Instance>> descendantAddedEvent = new Event<>();
    private final Event<Consumer<Instance>> descendantSubbedEvent = new Event<>();

    protected String name;

    public Instance() {

        Instance.insert(this);

        this.configure();

        Instance.instanceCreatedEvent.fire(f -> f.accept(this));

        ReplicateService.Input.create(this);

    }

    public Instance(UUID identifier) {

        if (INSTANCES.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "The field 'identifier' has to be unique."
            );
        }

        Instance.insert(identifier, this);

        this.configure();

        Instance.instanceCreatedEvent.fire(f -> f.accept(this));

        ReplicateService.Input.create(this);

    }

    protected void configure() {
        this.name = this.getVariety();
    }

    protected abstract Instance duplicate();

    public void destroy() {

        for (Instance child: this.getChildren()) {
            child.destroy();
        }

        this.subParent();
        this.children.clear();

        Instance.extract(this);

        Instance.instanceDestroyedEvent.fire(f -> f.accept(this));

        ReplicateService.Input.destroy(this);

    }

    private void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public void setParent(Instance parent) {
        this.subParent();
        this.addParent(parent);
    }

    protected void addParent(Instance parent) {
        if (parent != null) {
            this.parent = parent;
            parent.addChild(this);
            parent.addDescendant(this);
        }
    }

    protected void subParent() {
        Instance parent = this.parent;
        if (parent != null) {
            this.parent = null;
            parent.subChild(this);
            parent.subDescendant(this);
        }
    }

    public Instance getParent() {
        return this.parent;
    }

    public <I> I getParent(Class<I> clazz) {
        if (clazz.isInstance(this.parent)) {
            return clazz.cast(this.parent);
        }
        return null;
    }

    public boolean isParent(Instance child) {
        return child.parent == this;
    }

    public List<Instance> getAncenstors() {
        List<Instance> list = new ArrayList<>();
        Instance ancestor = this.getParent();
        while (ancestor != null) {
            list.add(ancestor);
            ancestor = ancestor.parent;
        }
        return list;
    }

    public <I> List<I> getAncenstors(Class<I> clazz) {
        List<I> list = new ArrayList<>();
        Instance ancestor = this.getParent();
        while (ancestor != null) {
            if (clazz.isInstance(ancestor)) {
                list.add(clazz.cast(ancestor));
            }
            ancestor = ancestor.parent;
        }
        return list;
    }

    public boolean isAncestor(Instance descendant) {
        if (this.isParent(descendant)) {
            return true;
        } else if (descendant.parent != null) {
            return this.isDescendant(descendant.parent);
        } else {
            return false;
        }
    }

    protected void addChild(Instance child) {
        this.children.add(child);
        this.childAddedEvent.fire(f -> f.accept(child));
    }

    protected void subChild(Instance child) {
        this.children.remove(child);
        this.childSubbedEvent.fire(f -> f.accept(child));
    }

    public boolean isChild(Instance parent) {
        return parent == this.parent;
    }

    private void addDescendant(Instance descendant) {
        this.descendantAddedEvent.fire(f -> f.accept(descendant));
        if (this.parent != null) {
            this.parent.addDescendant(descendant);
        }
    }

    private void subDescendant(Instance descendant) {
        this.descendantSubbedEvent.fire(f -> f.accept(descendant));
        if (this.parent != null) {
            this.parent.subDescendant(descendant);
        }
    }

    public boolean isDescendant(Instance ancestor) {
        if (this.isChild(ancestor)) {
            return true;
        } else if (this.parent != null) {
            return this.parent.isDescendant(ancestor);
        } else {
            return false;
        }
    }

    public void onChildAdded(Consumer<Instance> function) {
        this.childAddedEvent.connect(function);
    }

    public void onChildSubbed(Consumer<Instance> function) {
        this.childSubbedEvent.connect(function);
    }

    public void onDescendantAdded(Consumer<Instance> function) {
        this.descendantAddedEvent.connect(function);
    }

    public void onDescendantSubbed(Consumer<Instance> function) {
        this.descendantSubbedEvent.connect(function);
    }

    public List<Instance> getChildren() {
        return new ArrayList<>(this.children);
    }

    public <I> List<I> getChildren(Class<I> clazz) {
        List<I> children = new ArrayList<>();
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

    public <I> I findChild(String name, Class<I> clazz) {
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

    public <I> List<I> getDescendants(Class<I> clazz) {
        List<I> descendants = new ArrayList<>();
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

    public <I> I findDescendant(String name, Class<I> clazz) {
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

    public String getVariety() {
        return this.getClass().getSimpleName();
    }

    public HashMap<String, Object> getDigitaly() {
        HashMap<String, Object> digitaly = new HashMap<>();
        digitaly.put("parent", this.getParent());
        digitaly.put("name", this.getName());
        return digitaly;
    }

    public Signature getSignature() {
        return new Signature(this);
    }

    public Bundlure getBundlure() {
        return new Bundlure(this);
    }

    public String toString() {
        return this.name;
    }

}
