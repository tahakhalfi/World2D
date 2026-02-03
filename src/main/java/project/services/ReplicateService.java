package project.services;

import project.enumerations.Authority;
import project.enumerations.Remote;
import project.hierarchies.Instance;
import project.launchers.Starter;
import project.launchers.Server;
import project.utilities.formating.Bundlure;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplicateService {

    public static class Input {

        public static <I extends Instance> I create(I instance) {

            if (Starter.getAuthority() == Authority.CLIENT) {
                return instance;
            }

            Server.broadcast(Remote.CREATE, new HashMap<>(Map.of(
                    "directory", Bundlure.format(instance.getClass().getName()),
                    "identifier", Bundlure.format(instance.getIdentifier())
            )));

            return instance;

        }

        public static <I extends Instance> I create(UUID reference, I instance) {

            if (Starter.getAuthority() == Authority.CLIENT) {
                return instance;
            }

            Server.unicast(reference, Remote.CREATE, new HashMap<>(Map.of(
                    "directory", Bundlure.format(instance.getClass().getName()),
                    "identifier", Bundlure.format(instance.getIdentifier())
            )));

            return instance;

        }

        public static <I extends Instance> I destroy(I instance) {

            if (Starter.getAuthority() == Authority.CLIENT) {
                return instance;
            }

            Server.broadcast(Remote.DESTROY, new HashMap<>(Map.of(
                    "instance", Bundlure.format(instance)
            )));

            return instance;

        }

        public static <I extends Instance> I destroy(UUID reference, I instance) {

            if (Starter.getAuthority() == Authority.CLIENT) {
                return instance;
            }

            Server.unicast(reference, Remote.DESTROY, new HashMap<>(Map.of(
                    "instance", Bundlure.format(instance)
            )));

            return instance;

        }

    }

    public static class Output {

        public static void create(String directory, UUID identifier) throws Exception {

            Class<?> clazz = Class.forName(directory);

            Constructor<?> constructor = clazz.getDeclaredConstructor(UUID.class);

            constructor.newInstance(identifier);

        }

        public static void destroy(Instance instance) {

            if (instance == null) {
                return;
            }

            instance.destroy();

        }

    }

}
