package project.primitives;

public class Vector {

    // CLASS FUNCTIONS

    public static Vector parseVector(String content) {
        String[] split = content.split(", ");
        return new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }

    public static boolean equal(Vector vector, Vector operand) {
        return vector.getX() == operand.getX() && vector.getY() == operand.getY();
    }

    public static boolean equal(Vector vector, double operand) {
        return vector.getX() == operand && vector.getY() == operand;
    }

    public static boolean equal(Vector vector) {
        return Vector.equal(vector, 0);
    }

    public static Vector add(Vector vector, Vector operand) {
        return new Vector(vector.getX() + operand.getX(), vector.getY() + operand.getY());
    }

    public static Vector add(Vector vector, double operand) {
        return new Vector(vector.getX() + operand, vector.getY() + operand);
    }

    public static Vector sub(Vector vector, Vector operand) {
        return new Vector(vector.getX() - operand.getX(), vector.getY() - operand.getY());
    }

    public static Vector sub(Vector vector, double operand) {
        return new Vector(vector.getX() - operand, vector.getY() - operand);
    }

    public static Vector mul(Vector vector, Vector operand) {
        return new Vector(vector.getX() * operand.getX(), vector.getY() * operand.getY());
    }

    public static Vector mul(Vector vector, double operand) {
        return new Vector(vector.getX() * operand, vector.getY() * operand);
    }

    public static Vector div(Vector vector, Vector operand) {
        return new Vector(vector.getX() / operand.getX(), vector.getY() / operand.getY());
    }

    public static Vector div(Vector vector, double operand) {
        return new Vector(vector.getX() / operand, vector.getY() / operand);
    }

    // INSTANCE FUNCTIONS

    private final double x;
    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double n) {
        this(n, n);
    }

    public Vector() {
        this(0);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getSize() {
        return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    public double getAngle() {
        return Math.atan2(this.getY(), this.getX());
    }

    public Vector getUnit() {
        return Vector.div(this, this.getSize());
    }

    public String toString() {
        return this.getX() + ", " + this.getY();
    }

}
