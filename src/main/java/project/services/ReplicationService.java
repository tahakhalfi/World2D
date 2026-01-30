package project.services;

import project.enumerations.Remote;
import project.hierarchies.Instance;
import project.launchers.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplicationService {

    public static <C extends Instance> void configurateCreation(UUID identification, Class<C> clazz) {

        if (!Server.is()) {
            return;
        }

        Server.broadcast(Remote.CREATE, new HashMap<>(Map.of(
                "identification", identification,
                "clazz", clazz.getName()
        )));

    }

    public static <C extends Instance> void replicateCreation(UUID identification, Class<C> clazz) {

        System.out.println(identification);

        Instance.create(
                identification,
                clazz
        );

    }

    public static void configurateDestruction(UUID identification) {

        if (!Server.is()) {
            return;
        }

        Server.broadcast(Remote.DESTROY, new HashMap<>(Map.of(
                "identification", identification
        )));

    }

    public static void replicateDestruction(UUID identification) {

    }

}
