package project.launchers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import project.hierarchies.Instance;
import project.managers.CManager;
import project.enumerations.Remote;
import project.services.ConsoleService;
import project.services.ReplicateService;
import project.utilities.formating.Bundlure;

public final class Client {

    private static final Gson GSON = new Gson();

    private static String ADDRESS;
    private static Integer PORT;

    private static Socket SOCKET;
    private static DataInputStream INPUT;
    private static DataOutputStream OUTPUT;

    private static UUID REFERENCE;

    private static CManager MANAGER;

    public static void initiate(String address, Integer port, String token) throws Exception {

        ConsoleService.println("--- [CLIENT] ---", ConsoleService.BLUE);

        Client.ADDRESS = address;
        Client.PORT = port;

        Client.SOCKET = new Socket(
                ADDRESS,
                PORT
        );

        if (!Client.check()) {
            return;
        }

        Client.INPUT = new DataInputStream(SOCKET.getInputStream());
        Client.OUTPUT = new DataOutputStream(SOCKET.getOutputStream());

        Client.write(token);

        if (!Client.check()) {
            return;
        }

        new Thread(() -> {
            try {
                while (Client.check()) {Client.receive();}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        Client.MANAGER = new CManager();
        Client.MANAGER.config();

    }

    private static boolean check() {
        return SOCKET != null && !SOCKET.isClosed() && SOCKET.isConnected();
    }

    public static String getAddress() {
        return ADDRESS;
    }

    public static Integer getPort() {
        return PORT;
    }

    private static void setReference(UUID reference) {
        Client.REFERENCE = reference;
    }

    public static UUID getReference() {
        return Client.REFERENCE;
    }

    public static CManager getManager() {
        return MANAGER;
    }

    public static void broadcast(Remote remote, HashMap<String, Object> content) throws Exception {
        deliver(remote, content);
    }

    private static String read() throws Exception {
        return INPUT.readUTF();
    }

    private static void write(String data) throws Exception {
        OUTPUT.writeUTF(data);
        OUTPUT.flush();
    }

    private static void receive() throws Exception {

        JsonObject mail = GSON.fromJson(read(), JsonObject.class);

        Remote remote = GSON.fromJson(
                mail.get("remote"),
                Remote.class
        );

        JsonObject content = GSON.fromJson(
                mail.get("content"),
                JsonObject.class
        );

        switch (remote) {

            case Remote.REFERENCE -> Client.setReference(
                    UUID.fromString(content.get("reference").getAsString())
            );

            case Remote.CREATE -> ReplicateService.Output.create(
                    Bundlure.object(content.get("directory").getAsString(), String.class),
                    Bundlure.object(content.get("identifier").getAsString(), UUID.class)
            );

            case Remote.DESTROY -> ReplicateService.Output.destroy(
                    Bundlure.object(content.get("instance").getAsString(), Instance.class)
            );

        }

    }

    private static void deliver(Remote remote, HashMap<String, Object> content) throws Exception {

        HashMap<String, Object> mail = new HashMap<>();
        mail.put("remote", remote);
        mail.put("content", content);

        write(GSON.toJson(mail));

    }

}
