package project.launchers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import project.hierarchies.Instance;
import project.managers.CManager;
import project.enumerations.Remote;
import project.services.ConsoleService;
import project.services.ReplicationService;

public class Client {

    private static final Gson GSON = new Gson();

    private static boolean STATE = false;

    private static String ADDRESS;
    private static Integer PORT;

    private static Socket SOCKET;
    private static DataInputStream INPUT;
    private static DataOutputStream OUTPUT;

    private static Integer IDENTIFICATION;

    private static CManager MANAGER;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        initiate(String.valueOf(args[0]), Integer.valueOf(args[1]), String.valueOf(args[2]));
    }

    public static void initiate(String address, Integer port, String token) throws IOException, ClassNotFoundException {

        ConsoleService.println("--- [CLIENT] ---", ConsoleService.BLUE);

        STATE = true;

        ADDRESS = address;
        PORT = port;

        SOCKET = new Socket(
                ADDRESS,
                PORT
        );

        if (!check()) {
            return;
        }

        INPUT = new DataInputStream(SOCKET.getInputStream());
        OUTPUT = new DataOutputStream(SOCKET.getOutputStream());

        write(token);

        if (!check()) {
            return;
        }

        new Thread(() -> {
            try {
                while (check()) {receive();}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        MANAGER = new CManager();
        MANAGER.config();

    }

    public static boolean is() {
        return STATE && check();
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

    private static void enroll(int identification) {
        IDENTIFICATION = identification;
    }

    public static Integer getIdentification() {
        return IDENTIFICATION;
    }

    public static CManager getManager() {
        return MANAGER;
    }

    public static void broadcast(Remote remote, HashMap<String, Object> content) throws IOException {
        deliver(remote, content);
    }

    private static String read() throws IOException {
        return INPUT.readUTF();
    }

    private static void write(String data) throws IOException {
        OUTPUT.writeUTF(data);
        OUTPUT.flush();
    }

    private static void receive() throws IOException, ClassNotFoundException {

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

            case Remote.ENROLL -> enroll(
                    content.get("identification").getAsInt()
            );

            case Remote.CREATE -> ReplicationService.replicateCreation(
                    UUID.fromString(content.get("identification").getAsString()),
                    Class.forName(content.get("clazz").getAsString()).asSubclass(Instance.class)
            );

            case Remote.DESTROY -> {}
            case Remote.UPDATE -> {}
            case Remote.CUSTOM -> {}

        }

    }

    private static void deliver(Remote remote, HashMap<String, Object> content) throws IOException {

        HashMap<String, Object> mail = new HashMap<>();
        mail.put("remote", remote);
        mail.put("content", content);

        write(GSON.toJson(mail));

    }

}
