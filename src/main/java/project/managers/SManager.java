package project.managers;

import project.enumerations.Remote;
import project.hierarchies.Instance;
import project.hierarchies.instances.essentials.Experience;
import project.hierarchies.instances.essentials.groups.Groupspace;
import project.hierarchies.instances.essentials.groups.Depotspace;
import project.hierarchies.instances.essentials.groups.Scenespace;
import project.hierarchies.instances.essentials.groups.Worldspace;
import project.hierarchies.instances.peripherals.utilities.Player;
import project.services.ReplicateService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SManager {

    public void config() {
        this.start();
    }

    public void start() {

        Experience experience = new Experience();

        Worldspace worldspace = new Worldspace();
        worldspace.setParent(experience);

        Depotspace depotspace = new Depotspace();
        depotspace.setParent(experience);

        Scenespace scenespace = new Scenespace();
        scenespace.setParent(experience);

        Groupspace groupspace = new Groupspace();
        groupspace.setParent(experience);

    }

    public void connect(UUID reference) {

        for (Instance instance: Experience.get().getDescendants()) {
            ReplicateService.Input.create(reference, instance);
        }

        Player player = new Player();
        player.setReference(reference);
        player.setParent(Groupspace.get());

    }
    
    public void disconnect(UUID reference) {

    }

}
