package Purpose_code_generator;

public class GenStruct {
    private int label;
    private char[] op = new char[4];
    private int code;
    private int addr1;
    private int addr2;
    private int result;
    private int out_port;

    public GenStruct() {
    }

    public GenStruct(int label,int addr1, int addr2, int result) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.result = result;
    }

    public int getLabel() {
        return label;
    }

    public char[] getOp() {
        return op;
    }

    public int getCode() {
        return code;
    }

    public int getAddr1() {
        return addr1;
    }

    public int getAddr2() {
        return addr2;
    }

    public int getResult() {
        return result;
    }

    public int getOut_port() {
        return out_port;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setOp(char[] op) {
        this.op = op;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setAddr1(int addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(int addr2) {
        this.addr2 = addr2;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setOut_port(int out_port) {
        this.out_port = out_port;
    }

    @Override
    public String toString() {
        return "GenStruct{label" +
                "=" + label +
                ", op:" + op.toString() +
                ", addr1=" + addr1 +
                ", addr2=" + addr2 +
                ", result" + result +
                ", out_port" + out_port +
                '}';
    }
}
