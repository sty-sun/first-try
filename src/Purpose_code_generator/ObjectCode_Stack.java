package Purpose_code_generator;

public class ObjectCode_Stack {
    private int label = 0;
    private String op;
    private String Oop;
    private String Sop;

    public ObjectCode_Stack() {
    }

    public int getLabel() {
        return label;
    }

    public String getOp() {
        return op;
    }

    public String getOop() {
        return Oop;
    }

    public String getSop() {
        return Sop;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setOop(String oop) {
        Oop = oop;
    }

    public void setSop(String sop) {
        Sop = sop;
    }

    @Override
    public String toString(){
        if (label == 0) {
            String str = "   (" + op + " ";
            if (!Oop.equals("null")) {
                str += Oop;
            }
            if (!Sop.equals("null")) {
                str += ' ';
                str += Sop;
            }
            str += ')';
            return str;
        }
        else{
            return String.valueOf(label)+":";
        }
    }
}
