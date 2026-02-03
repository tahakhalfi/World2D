package project.launchers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import project.managers.SManager;
import project.enumerations.Remote;
import project.services.ConsoleService;

public final class Server {

    private static final Gson GSON = new Gson();

    private static String ADDRESS;
    private static Integer PORT;

    private static final String TOKEN = "xhtrtjcykv436uliboibh54lgkyjf76897m0hgn7gmhnjfmnjkyiyuo";

    private static ServerSocket SOCKET;

    private static HashMap<UUID, Connection> CONNECTIONS;

    private static SManager MANAGER;

    public static void initiate(String address, Integer port) throws Exception {

        ConsoleService.println("--- [SERVER] ---", ConsoleService.GREEN);

        Server.ADDRESS = address;
        Server.PORT = port;

        Server.SOCKET = new ServerSocket();
        Server.SOCKET.bind(new InetSocketAddress(ADDRESS, PORT));

        if (!Server.check()) {
            return;
        }

        Server.CONNECTIONS = new HashMap<>();

        new Thread(() -> {
            try {
                while (Server.check()) {Server.connect(SOCKET.accept());}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        Server.MANAGER = new SManager();
        Server.MANAGER.config();

    }

    private static boolean check() {
        return Server.SOCKET != null && !Server.SOCKET.isClosed();
    }

    public static SManager getManager() {
        return Server.MANAGER;
    }

    private static void connect(Socket socket) throws Exception {

        Connection connection = new Connection(socket);

        if (!connection.read().equals(Server.TOKEN)) {

            connection.close();

        } else {

            connection.setReference(UUID.randomUUID());

            Server.addConnection(connection);

            connection.deliver(Remote.REFERENCE, new HashMap<>(Map.of(
                    "reference", connection.getReference()
            )));

            connection.start();

            Server.MANAGER.connect(connection.getReference());

        }

    }

    private static List<Connection> getConnections() {
        return new ArrayList<>(CONNECTIONS.values());
    }

    private static void addConnection(Connection connection) {
        Server.CONNECTIONS.put(connection.getReference(), connection);
    }

    private static void subConnection(Connection connection) {
        Server.CONNECTIONS.remove(connection.getReference());
    }

    private static Connection getConnection(UUID reference) {
        return Server.CONNECTIONS.get(reference);
    }

    public static void cast(Remote remote, HashMap<String, Object> content) {
        Server.broadcast(remote, content);
    }

    public static void cast(Set<UUID> references, Remote remote, HashMap<String, Object> content) {
        Server.multicast(references, remote, content);
    }

    public static void cast(UUID reference, Remote remote, HashMap<String, Object> content) {
        Server.unicast(reference, remote, content);
    }

    public static void broadcast(Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: Server.getConnections()) {
                connection.deliver(remote, content);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void multicast(Set<UUID> references, Remote remote, HashMap<String, Object> content) {
        multicastIn(references, remote, content);
    }

    public static void multicastIn(Set<UUID> references, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: Server.getConnections()) {
                if (references.contains(connection.getReference())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void multicastOut(Set<UUID> references, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: Server.getConnections()) {
                if (!references.contains(connection.getReference())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void unicast(UUID reference, Remote remote, HashMap<String, Object> content) {
        unicastIn(reference, remote, content);
    }

    public static void unicastIn(UUID reference, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: Server.getConnections()) {
                if (reference.equals(connection.getReference())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void unicastOut(UUID reference, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: Server.getConnections()) {
                if (!reference.equals(connection.getReference())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class Connection extends Thread {

        private Socket SOCKET;
        private DataInputStream INPUT;
        private DataOutputStream OUTPUT;

        private UUID REFERENCE;

        private Connection(Socket socket) throws Exception {

            this.SOCKET = socket;

            this.INPUT = new DataInputStream(socket.getInputStream());
            this.OUTPUT = new DataOutputStream(socket.getOutputStream());

        }

        private void close() throws Exception {
            this.SOCKET.close();
        }

        public boolean check() {
            return this.SOCKET != null && !this.SOCKET.isClosed() && this.SOCKET.isConnected();
        }

        public void run() {
            while (check()) {
                try {
                    this.receive();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void setReference(UUID identifier) {
            this.REFERENCE = identifier;
        }

        public UUID getReference() {
            return this.REFERENCE;
        }

        private String read() throws Exception {
            return this.INPUT.readUTF();
        }

        private void write(String data) throws Exception {
            this.OUTPUT.writeUTF(data);
            this.OUTPUT.flush();
        }

        private void receive() throws Exception {

            JsonObject mail = GSON.fromJson(this.read(), JsonObject.class);

            Remote remote = GSON.fromJson(
                    mail.get("remote"),
                    Remote.class
            );

            HashMap<String, Object> content = GSON.fromJson(
                    mail.get("content"),
                    HashMap.class
            );

        }

        private void deliver(Remote remote, HashMap<String, Object> content) throws Exception {

            HashMap<String, Object> mail = new HashMap<>();
            mail.put("remote", remote);
            mail.put("content", content);

            this.write(GSON.toJson(mail));

        }

    }

}
