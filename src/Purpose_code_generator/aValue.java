package Purpose_code_generator;

import java.util.ArrayList;

public class aValue {
    private String name;
    private ArrayList<String> location;

    public aValue() {
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String name) {
        location = new ArrayList<>();
        location.add(name);
    }
}
