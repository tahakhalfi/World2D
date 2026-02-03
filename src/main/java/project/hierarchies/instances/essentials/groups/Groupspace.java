package project.hierarchies.instances.essentials.groups;

import project.hierarchies.Instance;
import project.hierarchies.instances.Essential;
import project.hierarchies.instances.peripherals.utilities.Player;
import project.utilities.signaling.Event;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class Groupspace extends Essential {

    // CLASS FUNCTIONS

    public static Groupspace get() {
        return Essential.get(Groupspace.class);
    }

    // INSTANCE FUNCTIONS

    private final HashMap<UUID, Player> players = new HashMap<>();

    private final Event<Consumer<Player>> playerAddedEvent = new Event<>();
    private final Event<Consumer<Player>> playerSubbedEvent = new Event<>();

    public Groupspace() {
        super();
    }

    public Groupspace(UUID identifier) {
        super(identifier);
    }

    protected void addChild(Instance child) {
        super.addChild(child);
        if (child instanceof Player player) {
            this.addPlayer(player);
        }
    }

    protected void subChild(Instance child) {
        super.subChild(child);
        if (child instanceof Player player) {
            this.subPlayer(player);
        }
    }

    private void addPlayer(Player player) {
        this.players.put(player.getReference(), player);
        this.playerAddedEvent.fire(f -> f.accept(player));
    }

    private void subPlayer(Player player) {
        this.players.remove(player.getReference());
        this.playerSubbedEvent.fire(f -> f.accept(player));
    }

    public void onPlayerAdded(Consumer<Player> function) {
        this.playerAddedEvent.connect(function);
    }

    public void onPlayerSubbed(Consumer<Player> function) {
        this.playerSubbedEvent.connect(function);
    }

}
