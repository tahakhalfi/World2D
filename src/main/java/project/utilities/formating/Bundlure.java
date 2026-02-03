package project.utilities.formating;

import project.hierarchies.Instance;
import project.primitives.Color;
import project.primitives.Udimor;
import project.primitives.Vector;

import java.util.UUID;

public class Bundlure {

    public static String fetch(String text) {

        int middle = text.indexOf('(');

        if (middle == -1) {
            return null;
        }

        int start = 0;

        for (int index = middle - 1; index >= 0; index--) {
            if (!Character.isLetterOrDigit(text.charAt(index))) {
                start = index + 1;
                break;
            }
        }

        if (start == middle) {
            return null;
        }

        int end = text.length() - 1;

        StringBuilder builder = new StringBuilder();

        int depth = 1;

        for (int index = middle + 1; index < text.length(); index++) {

            char character = text.charAt(index);

            if (character == '(') {
                depth++;
            } else if (character == ')') {
                depth--;
            }

            if (depth == 0) {
                end = index;
                break;
            }

        }

        if (depth > 0) {
            return null;
        }

        return text.substring(start, end + 1);

    }

    public static String format(Object object) {
        return new Bundlure(object).toFormat();
    }

    public static Object value(String format) {
        return new Bundlure(format).getValue();
    }

    public static <C> C object(String format, Class<C> clazz) {
        return new Bundlure(format).toObject(clazz);
    }

    // INSTANCE FUNCTIONS

    private String label;
    private Object value;

    public Bundlure(Object object) {

        if (object == null) {
            this.setLabel("Null");
            this.setValue("");
        } else {
            switch (object) {
                case Byte value -> {
                    this.setLabel("Byte");
                    this.setValue(value);
                }
                case Short value -> {
                    this.setLabel("Short");
                    this.setValue(value);
                }
                case Integer value -> {
                    this.setLabel("Integer");
                    this.setValue(value);
                }
                case Long value -> {
                    this.setLabel("Long");
                    this.setValue(value);
                }
                case Float value -> {
                    this.setLabel("Float");
                    this.setValue(value);
                }
                case Double value -> {
                    this.setLabel("Double");
                    this.setValue(value);
                }
                case Character value -> {
                    this.setLabel("Character");
                    this.setValue(value);
                }
                case String value -> {
                    this.setLabel("String");
                    this.setValue(value);
                }
                case Boolean value -> {
                    this.setLabel("Boolean");
                    this.setValue(value);
                }
                case Vector value -> {
                    this.setLabel("Vector");
                    this.setValue(value);
                }
                case Udimor value -> {
                    this.setLabel("Udimor");
                    this.setValue(value);
                }
                case Color value -> {
                    this.setLabel("Color");
                    this.setValue(value);
                }
                case UUID value -> {
                    this.setLabel("UUID");
                    this.setValue(value);
                }
                case Instance value -> {
                    this.setLabel("Instance");
                    this.setValue(value);
                }
                default -> throw new IllegalArgumentException("The object cannot be registered.");
            }
        }

    }

    public Bundlure(String format) {

        int start = format.indexOf('(');

        if (start == -1) {
            return;
        }

        int end = format.length() - 1;

        if (format.charAt(end) != ')') {
            return;
        }

        String label = format.substring(0, start);
        String content = format.substring(start + 1, end);

        Object value = switch (label) {
            case "Byte" -> Byte.parseByte(content);
            case "Short" -> Short.parseShort(content);
            case "Integer" -> Integer.parseInt(content);
            case "Long" -> Long.parseLong(content);
            case "Float" -> Float.parseFloat(content);
            case "Double" -> Double.parseDouble(content);
            case "Character" -> content.charAt(0);
            case "String" -> content;
            case "Boolean" -> Boolean.parseBoolean(content);
            case "Vector" -> Vector.parseVector(content);
            case "Udimor" -> Udimor.parseUdimor(content);
            case "Color" -> Color.parseColor(content);
            case "UUID" -> UUID.fromString(content);
            case "Instance" -> Instance.obtain(UUID.fromString(content));
            case "Null" -> null;
            default -> throw new IllegalArgumentException("The format is cannot be registered.");
        };

        this.setLabel(label);
        this.setValue(value);

    }

    private void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    private void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public <C> C toObject(Class<C> clazz) {

        Object value = this.getValue();

        if (!clazz.isInstance(value)) {
            return null;
        }

        return clazz.cast(value);

    }

    public String toFormat() {
        return this.getLabel() + "(" + this.getValue() + ")";
    }

}
