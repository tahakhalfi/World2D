package project.launchers;

import project.enumerations.Authority;

public class Starter {

    private static Authority AUTHORITY;

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            throw new IllegalArgumentException("Needs arguments [SERVER ...] or [CLIENT ...]");
        }

        Starter.AUTHORITY = Authority.valueOf(args[0]);

        if (Starter.AUTHORITY == Authority.SERVER) {
            Server.initiate(args[1], Integer.parseInt(args[2]));
        } else if (Starter.AUTHORITY == Authority.CLIENT) {
            Client.initiate(args[1], Integer.parseInt(args[2]), args[3]);
        }

    }

    public static Authority getAuthority() {
        return Starter.AUTHORITY;
    }

}
