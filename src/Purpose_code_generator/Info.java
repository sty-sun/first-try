package Purpose_code_generator;

import java.util.ArrayList;

public class Info {
    private String name;
    private ArrayList<Integer> location;

    public Info() {
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Integer> location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

}
