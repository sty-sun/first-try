package Purpose_code_generator;

public class ObjectCode_Stack {
    private String op;
    private String Oop;
    private String Sop;

    public ObjectCode_Stack() {
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
        if (!op.equals("jmp")) {
            return "ObjectCode_Stack{" +
                    " " + op +
                    "  " + Oop +
                    " , " + Sop +
                    '}';
        }
        else {
            return "ObjectCode_Stack{" +
                    " " + op +
                    "  " + Oop;
        }
    }
}
