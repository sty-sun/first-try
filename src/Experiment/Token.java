package Experiment;

public class Token {
    private int label;
    private String name;
    private int code;
    private int address;

    public Token() {
    }

    @Override
    public String toString() {
        return "Token{" +
                "label=" + label +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", address=" + address +
                '}';
    }

    public Token(int label, String name, int code, int address) {
        this.label = label;
        this.name = name;
        this.code = code;
        this.address = address;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
