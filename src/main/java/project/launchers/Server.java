package project.launchers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import project.managers.SManager;
import project.enumerations.Remote;
import project.services.ConsoleService;

public final class Server {

    private static final Gson GSON = new Gson();

    private static boolean STATE = false;

    private static String ADDRESS;
    private static Integer PORT;

    private static final String TOKEN = "xhtrtjcykv436uliboibh54lgkyjf76897m0hgn7gmhnjfmnjkyiyuo";

    private static ServerSocket SOCKET;

    private static LinkedList<Connection> CONNECTIONS;

    private static SManager MANAGER;

    public static void main(String[] args) throws IOException {
        initiate(String.valueOf(args[0]), Integer.valueOf(args[1]));
    }

    public static void initiate(String address, Integer port) throws IOException {

        ConsoleService.println("--- [SERVER] ---", ConsoleService.GREEN);

        STATE = true;

        ADDRESS = address;
        PORT = port;

        SOCKET = new ServerSocket();
        SOCKET.bind(new InetSocketAddress(ADDRESS, PORT));

        if (!check()) {
            return;
        }

        CONNECTIONS = new LinkedList<>();

        new Thread(() -> {
            try {
                while (check()) {connect(SOCKET.accept());}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        MANAGER = new SManager();
        MANAGER.config();

    }

    public static boolean is() {
        return STATE && check();
    }

    private static boolean check() {
        return SOCKET != null && !SOCKET.isClosed();
    }

    public static SManager getManager() {
        return MANAGER;
    }

    private static void connect(Socket socket) throws IOException {

        Connection connection = new Connection(socket);

        if (!connection.read().equals(TOKEN)) {

            connection.close();

        } else {

            int identification = CONNECTIONS.size();

            connection.enroll(identification);

            connection.deliver(Remote.ENROLL, new HashMap<>(Map.of(
                    "identification", identification
            )));

            CONNECTIONS.add(connection);

            connection.start();

        }

    }

    public static void broadcast(Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: CONNECTIONS) {
                connection.deliver(remote, content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void multicast(Set<Integer> identifications, Remote remote, HashMap<String, Object> content) {
        multicastIn(identifications, remote, content);
    }

    public static void multicastIn(Set<Integer> identifications, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: CONNECTIONS) {
                if (identifications.contains(connection.getIdentification())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void multicastOut(Set<Integer> identifications, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: CONNECTIONS) {
                if (!identifications.contains(connection.getIdentification())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unicast(Integer identification, Remote remote, HashMap<String, Object> content) {
        unicastIn(identification, remote, content);
    }

    public static void unicastIn(Integer identification, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: CONNECTIONS) {
                if (identification.equals(connection.getIdentification())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unicastOut(Integer identification, Remote remote, HashMap<String, Object> content) {
        try {
            for (Connection connection: CONNECTIONS) {
                if (!identification.equals(connection.getIdentification())) {
                    connection.deliver(remote, content);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Connection extends Thread {

        private Socket SOCKET;
        private DataInputStream INPUT;
        private DataOutputStream OUTPUT;

        private Integer IDENTIFICATION;

        private Connection(Socket socket) throws IOException {

            this.SOCKET = socket;

            this.INPUT = new DataInputStream(socket.getInputStream());
            this.OUTPUT = new DataOutputStream(socket.getOutputStream());

        }

        private void close() throws IOException {
            this.SOCKET.close();
        }

        public boolean check() {
            return this.SOCKET != null && !this.SOCKET.isClosed() && this.SOCKET.isConnected();
        }

        public void run() {
            while (check()) {
                try {
                    this.receive();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void enroll(int identification) {
            this.IDENTIFICATION = identification;
        }

        public Integer getIdentification() {
            return this.IDENTIFICATION;
        }

        private String read() throws IOException {
            return this.INPUT.readUTF();
        }

        private void write(String data) throws IOException {
            this.OUTPUT.writeUTF(data);
            this.OUTPUT.flush();
        }

        private void receive() throws IOException {

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

        private void deliver(Remote remote, HashMap<String, Object> content) throws IOException {

            HashMap<String, Object> mail = new HashMap<>();
            mail.put("remote", remote);
            mail.put("content", content);

            this.write(GSON.toJson(mail));

        }

    }

}
