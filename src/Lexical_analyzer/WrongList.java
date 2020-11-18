package Lexical_analyzer;

public class WrongList {
    private int Line;
    private int num;
    private String Description;

    public WrongList() {
    }

    public WrongList(int line, String description) {
        Line = line;
        Description = description;
    }

    public WrongList(int line, int num, String description) {
        Line = line;
        this.num = num;
        Description = description;
    }

    public int getLine() {
        return Line;
    }

    public int getNum() {
        return num;
    }

    public String getDescription() {
        return Description;
    }

    public void setLine(int line) {
        Line = line;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
