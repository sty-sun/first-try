package Lexical_analyzer;

public class Symble {
    private int number;
    private int type;
    private String name;

    @Override
    public String toString() {
        return "Symble{" +
                "number=" + number +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }

    public Symble() {
    }

    public Symble(int number, int type, String name) {
        this.number = number;
        this.type = type;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
