package Purpose_code_generator;

import java.util.ArrayList;

public class Info {
    private String name;
    private ArrayList<Integer> location;

    public Info() {
        location = new ArrayList<>();
        location.add(-1);
    }

    public Info(String name) {
        location = new ArrayList<>();
        location.add(-1);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(ArrayList<Integer> location) {
        this.location = location;
    }

    @Override
    public String toString(){
        String str = "" + name + "-->";
        for (int i:location){
            str += " " + String.valueOf(i);
        }
        return str;
    }
}
