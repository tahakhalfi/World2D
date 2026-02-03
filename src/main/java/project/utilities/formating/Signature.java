package project.utilities.formating;

import project.hierarchies.Instance;
import project.hierarchies.instances.essentials.groups.Groupspace;
import project.hierarchies.instances.essentials.groups.Depotspace;
import project.hierarchies.instances.essentials.Experience;
import project.hierarchies.instances.essentials.groups.Worldspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signature {

    private String variety;
    private HashMap<String, Object> digitaly;

    public Signature(String variety, HashMap<String, Object> digitaly) {

        this.variety = variety;
        this.digitaly = digitaly;

    }

    public Signature(Instance instance) {

        this.variety = instance.getVariety();
        this.digitaly = instance.getDigitaly();

    }

    public Signature(String format) {

        int start = format.indexOf('[');

        if (start == -1) {
            return;
        }

        int end = format.length() - 1;

        if (format.charAt(end) != ']') {
            return;
        }

        String variety = format.substring(0, start);

        HashMap<String, Object> digitaly = new HashMap<>();

        String recipe = format.substring(start + 1, end);

        int index0 = 0;

        while (true) {

            int index1 = recipe.indexOf(": ", index0);

            if (index1 == -1) {
                break;
            }

            String label = recipe.substring(index0, index1);

            int index2 = index1 + 2;

            String bundlureFormat = Bundlure.fetch(recipe.substring(index2));

            if (bundlureFormat == null) {
                break;
            }

            Bundlure bundlure = new Bundlure(bundlureFormat);

            digitaly.put(label, bundlure.getValue());

            int index3 = index2 + bundlureFormat.length() + 2;

            if (index3 >= recipe.length()) {
                break;
            }

            index0 = index3;

        }

        this.variety = variety;
        this.digitaly = digitaly;

    }

    public String getVariety() {
        return this.variety;
    }

    public HashMap<String, Object> getDigitaly() {
        return new HashMap<>(this.digitaly);
    }

    public String getRecipe() {

        List<Map.Entry<String, Object>> list = new ArrayList<>(this.digitaly.entrySet());

        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < list.size(); index++) {

            Map.Entry<String, Object> entry = list.get(index);

            String label = entry.getKey();
            Object value = entry.getValue();

            builder.append(label);
            builder.append(": ");

            Bundlure bundlure = new Bundlure(value);

            builder.append(bundlure.toFormat());

            if (index < list.size() - 1) {
                builder.append(", ");
            }

        }

        return builder.toString();

    }

    public Instance toInstance() {

        Instance instance = switch (this.getVariety()) {
            case "Experience" -> new Experience();
            case "Worldspace" -> new Worldspace();
            case "Crowdspace" -> new Groupspace();
            case "Depotspace" -> new Depotspace();
            default -> null;
        };

        return instance;

    }
    
    public String toFormat() {
        return this.getVariety() + "[" + this.getRecipe() + "]";
    }

    public String toString() {
        return this.toFormat();
    }

}
