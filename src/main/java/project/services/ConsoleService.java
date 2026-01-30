package project.services;

public class ConsoleService {

    public static final String DEFAULT = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static void println(Object subject, String color) {
        System.out.println(color + subject.toString() + DEFAULT);
    }

    public static void println(Object subject) {
        println(subject, DEFAULT);
    }

    public static void print(Object subject, String color) {
        System.out.print(color + subject.toString() + DEFAULT);
    }

    public static void print(Object subject) {
        print(subject, DEFAULT);
    }

}
