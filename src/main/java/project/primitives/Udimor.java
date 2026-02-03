package project.primitives;

public class Udimor {

    // CLASS FUNCTIONS

    public static Udimor parseUdimor(String content) {
        String[] split = content.split(", ");
        return new Udimor(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }

    public static boolean equal(Udimor udimor, Udimor operand) {
        return udimor.getX() == operand.getX() && udimor.getY() == operand.getY();
    }

    public static boolean equal(Udimor udimor, double operand) {
        return udimor.getX() == operand && udimor.getY() == operand;
    }

    public static boolean equal(Udimor udimor) {
        return Udimor.equal(udimor, 0);
    }

    public static Udimor add(Udimor udimor, Udimor operand) {
        return new Udimor(udimor.getX() + operand.getX(), udimor.getY() + operand.getY());
    }

    public static Udimor add(Udimor udimor, double operand) {
        return new Udimor(udimor.getX() + operand, udimor.getY() + operand);
    }

    public static Udimor sub(Udimor udimor, Udimor operand) {
        return new Udimor(udimor.getX() - operand.getX(), udimor.getY() - operand.getY());
    }

    public static Udimor sub(Udimor udimor, double operand) {
        return new Udimor(udimor.getX() - operand, udimor.getY() - operand);
    }

    public static Udimor mul(Udimor udimor, Udimor operand) {
        return new Udimor(udimor.getX() * operand.getX(), udimor.getY() * operand.getY());
    }

    public static Udimor mul(Udimor udimor, double operand) {
        return new Udimor(udimor.getX() * operand, udimor.getY() * operand);
    }

    public static Udimor div(Udimor udimor, Udimor operand) {
        return new Udimor(udimor.getX() / operand.getX(), udimor.getY() / operand.getY());
    }

    public static Udimor div(Udimor udimor, double operand) {
        return new Udimor(udimor.getX() / operand, udimor.getY() / operand);
    }

    // INSTANCE FUNCTIONS

    private final double x;
    private final double y;

    public Udimor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Udimor(double n) {
        this(n, n);
    }

    public Udimor() {
        this(0);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Udimor getAdd(Udimor u) {
        return Udimor.add(this, u);
    }

    public Udimor getAdd(double n) {
        return Udimor.add(this, n);
    }

    public Udimor getSub(Udimor u) {
        return Udimor.sub(this, u);
    }

    public Udimor getSub(double n) {
        return Udimor.sub(this, n);
    }

    public Udimor getMul(Udimor u) {
        return Udimor.mul(this, u);
    }

    public Udimor getMul(double n) {
        return Udimor.mul(this, n);
    }

    public Udimor getDiv(Udimor u) {
        return Udimor.div(this, u);
    }

    public Udimor getDiv(double n) {
        return Udimor.div(this, n);
    }

    public double getSize() {
        return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    public double getAngle() {
        return Math.atan2(this.getY(), this.getX());
    }

    public Udimor getUnit() {
        return this.getDiv(this.getSize());
    }

    public String toString() {
        return this.getX() + ", " + this.getY();
    }

}
