package project.utilities.signaling;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Event<F> {

    private final ArrayList<F> FUNCTIONS = new ArrayList<>();

    public void connect(F function) {
        this.FUNCTIONS.add(function);
    }

    public void fire(Consumer<F> formula) {
        for (F function : this.FUNCTIONS) {
            formula.accept(function);
        }
    }

}