package project.primitives;

public class Color {

    // CLASS FUNCTIONS

    public static final Color BLACK = new Color(0);
    public static final Color WHITE = new Color(255);

    public static Color parseColor(String content) {
        String[] split = content.split(", ");
        return new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public static boolean equal(Color color, Color operand) {
        return color.getR() == operand.getR() && color.getG() == operand.getG() && color.getB() == operand.getB();
    }

    public static boolean equal(Color color, double operand) {
        return color.getR() == operand && color.getG() == operand && color.getB() == operand;
    }

    public static boolean equal(Color color) {
        return Color.equal(color, 0);
    }

    public static Color add(Color color, Color operand) {
        return new Color(
                color.getR() + operand.getR(),
                color.getG() + operand.getG(),
                color.getB() + operand.getB()
        );
    }

    public static Color add(Color color, double operand) {
        return new Color(
                color.getR() + operand,
                color.getG() + operand,
                color.getB() + operand
        );
    }

    public static Color add(Color color, int operand) {
        return Color.add(color, (double) operand);
    }

    public static Color sub(Color color, Color operand) {
        return new Color(
                color.getR() - operand.getR(),
                color.getG() - operand.getG(),
                color.getB() - operand.getB()
        );
    }

    public static Color sub(Color color, double operand) {
        return new Color(
                color.getR() - operand,
                color.getG() - operand,
                color.getB() - operand
        );
    }

    public static Color sub(Color color, int operand) {
        return Color.sub(color, (double) operand);
    }

    public static Color mul(Color color, Color operand) {
        return new Color(
                color.getR() * operand.getR() / 255.0,
                color.getG() * operand.getG() / 255.0,
                color.getB() * operand.getB() / 255.0
        );
    }

    public static Color mul(Color color, double operand) {
        return new Color(
                color.getR() * operand / 255.0,
                color.getG() * operand / 255.0,
                color.getB() * operand / 255.0
        );
    }

    public static Color mul(Color color, int operand) {
        return Color.mul(color, (double) operand);
    }

    public static Color div(Color color, Color operand) {
        return new Color(
                255.0 * color.getR() / operand.getR(),
                255.0 * color.getG() / operand.getG(),
                255.0 * color.getB() / operand.getB()
        );
    }

    public static Color div(Color color, double operand) {
        return new Color(
                255.0 * color.getR() / operand,
                255.0 * color.getG() / operand,
                255.0 * color.getB() / operand
        );
    }

    public static Color div(Color color, int operand) {
        return Color.div(color, (double) operand);
    }

    // INSTANCE FUNCTIONS

    private final int r;
    private final int g;
    private final int b;

    public Color(int r, int g, int b) {
        this.r = Math.max(0, Math.min(r, 255));
        this.g = Math.max(0, Math.min(g, 255));
        this.b = Math.max(0, Math.min(b, 255));
    }

    public Color(double r, double g, double b) {
        this((int) r, (int) g, (int) b);
    }

    public Color(int n) {
        this(n, n, n);
    }

    public Color(double n) {
        this((int) n);
    }

    public Color() {
        this(0);
    }

    public int getR() {
        return this.r;
    }

    public int getG() {
        return this.g;
    }

    public int getB() {
        return this.b;
    }

    public double getNR() {
        return (double) this.getR() / 255;
    }

    public double getNG() {
        return (double) this.getG() / 255;
    }

    public double getNB() {
        return (double) this.getB() / 255;
    }

    public String getHR() {
        return String.format("%02X", this.getR());
    }

    public String getHG() {
        return String.format("%02X", this.getG());
    }

    public String getHB() {
        return String.format("%02X", this.getB());
    }

    public String toByt() {
        return this.getR() + ", " + this.getG() + ", " + this.getB();
    }

    public String toNrm() {
        return this.getNR() + ", " + this.getNG() + ", " + this.getNB();
    }

    public String toHex() {
        return this.getHB() + this.getHG() + this.getHB();
    }

    public String toString() {
        return this.toByt();
    }

}
