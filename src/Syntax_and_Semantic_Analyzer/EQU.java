package Syntax_and_Semantic_Analyzer;

public class EQU {
    private double a;
    private int op;
    private int op1;
    private int op2;
    private int result;
    private int zzh;

    public EQU() {
    }

    public int getOp() {
        return op;
    }

    public int getOp1() {
        return op1;
    }

    public int getOp2() {
        return op2;
    }

    public int getResult() {
        return result;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public void setOp1(int op1) {
        this.op1 = op1;
    }

    public void setOp2(int op2) {
        this.op2 = op2;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
