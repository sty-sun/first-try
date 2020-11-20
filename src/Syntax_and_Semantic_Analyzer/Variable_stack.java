package Syntax_and_Semantic_Analyzer;

public class Variable_stack {
    private String idName;
    private String type;
    private int offset;

    public Variable_stack() {
    }

    public Variable_stack(String idName, String type, int offset) {
        this.idName = idName;
        this.type = type;
        this.offset = offset;
    }

    public String getIdName() {
        return idName;
    }

    public String getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Variable_stack{" +
                "IdName=" + idName +
                ", Type:" + type +
                ", offset=" + offset +
                '}';
    }
}
